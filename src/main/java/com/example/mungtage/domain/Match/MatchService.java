package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostRepository;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.model.MatchResult;
import com.example.mungtage.domain.Match.model.MatchTrial;
import com.example.mungtage.domain.Match.dto.MatchTrialDto;
import com.example.mungtage.domain.Match.dto.MatchResultDto;
import com.example.mungtage.util.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final ObjectMapper objectMapper;
    private final MatchTrialRepository matchTrialRepository;
    private final MatchResultRepository matchResultRepository;
    private final LostRepository lostRepository;

    public MatchTrial createMatchTrial(Long lostId) throws ChangeSetPersister.NotFoundException {
        MatchTrial matchTrial = new MatchTrial();

        Lost lost = lostRepository.findById(lostId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        matchTrial.setLost(lost);
        return matchTrialRepository.save(matchTrial);
    }

    public Boolean createMatchResults(MatchTrial matchTrial, List<Long> result) {
        for (int i=0; i<result.size(); i++) {
            MatchResult matchResult = new MatchResult(result.get(i), i+1);
            matchResult.setMatchTrial(matchTrial);
            matchResultRepository.save(matchResult);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public MatchTrialDto updateMatchTrialDone(Long matchTrialId) throws ChangeSetPersister.NotFoundException {
        MatchTrial matchTrial = matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        matchTrial.setIsDone(true);

        Lost lost = matchTrial.getLost();
        List<MatchResult> matchResults = matchTrial.getMatchResults();
        List<MatchResultDto> macthResultsResponse = matchResults
                .stream()
                .map(s -> MatchResultDto.from(s))
                .collect(Collectors.toList());

        MatchTrialDto matchTrialDto = new MatchTrialDto();
        matchTrialDto.setMatchTrialId(matchTrial.getId());
        matchTrialDto.setLostId(lost.getId());
        matchTrialDto.setIsDone(matchTrial.getIsDone());
        matchTrialDto.setMatchResults(macthResultsResponse);

        matchTrialRepository.save(matchTrial);

        return matchTrialDto;
    }

    public MatchTrial getMatchTrial(Long matchTrialId) throws ChangeSetPersister.NotFoundException{
        return matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Map<String,String> requestToAIServer(String imageUrl) throws URISyntaxException, HttpServerErrorException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        System.out.println(entity);
        String aiServerURL = "http://49.50.163.148:5000/pd/?img=";

        ResponseEntity<String> response = restTemplate.exchange(new URI(aiServerURL+imageUrl), HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("응답에 성공!! {}",response.getBody());
            try {
                Map<String, String> map = objectMapper.readValue(response.getBody(), Map.class);
                log.info("Json to Map 변환 성공!! {}",map);
                return map;
            } catch (IOException e) {
                throw new BadRequestException("변환 실패!!!");
            }
        } else {
            throw new HttpServerErrorException(response.getStatusCode(), "AI server request error");
        }
    }
}
