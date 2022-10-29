package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostRepository;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import com.example.mungtage.domain.Match.dto.MatchResultWithRescueDto;
import com.example.mungtage.domain.Match.model.MatchResult;
import com.example.mungtage.domain.Match.dto.MatchResultDto;
import com.example.mungtage.domain.Rescue.RescueService;
import com.example.mungtage.domain.Rescue.dto.RescueDto;
import com.example.mungtage.util.EmailService;
import com.example.mungtage.util.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {
    private final ObjectMapper objectMapper;
    private final MatchResultRepository matchResultRepository;
    private final RescueService rescueService;
    private final EmailService emailService;
    private final LostRepository lostRepository;

//    public MatchTrial createMatchTrial(Long lostId) throws ChangeSetPersister.NotFoundException {
//        MatchTrial matchTrial = new MatchTrial();
//
//        Lost lost = lostRepository.findById(lostId).orElseThrow(ChangeSetPersister.NotFoundException::new);
//        matchTrial.setLost(lost);
//        return matchTrialRepository.save(matchTrial);
//    }

    public Boolean createMatchResults(Lost lost, List<String> result) {
        System.out.println(result);
        for (int i=0; i<result.size(); i++) {
            MatchResult matchResult = new MatchResult(Long.parseLong(result.get(i)), i+1);
            matchResult.setLost(lost);
            matchResultRepository.save(matchResult);
        }
        return true;
    }

    public List<MatchResultDto> getLastMatchResults(Long lostId) {
        // PageRequest pageRequest = PageRequest.of(pageNum, 10, new Sort(Sort.Direction.DESC, "updated_date"));
        List<MatchResult> result = matchResultRepository.findByLostId(lostId);
        List<MatchResultDto> dto = result
                .stream()
                .map(s -> MatchResultDto.from(s))
                .collect(Collectors.toList());

        return dto;
    }

//    @Transactional(readOnly = true)
//    public MatchTrialDto updateMatchTrialDone(Long matchTrialId) throws ChangeSetPersister.NotFoundException {
//        MatchTrial matchTrial = matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
//        matchTrial.setIsDone(true);
//
//        Lost lost = matchTrial.getLost();
//        List<MatchResult> matchResults = matchTrial.getMatchResults();
//        List<MatchResultDto> macthResultsResponse = matchResults
//                .stream()
//                .map(s -> MatchResultDto.from(s))
//                .collect(Collectors.toList());
//
//        MatchTrialDto matchTrialDto = new MatchTrialDto();
//        matchTrialDto.setMatchTrialId(matchTrial.getId());
//        matchTrialDto.setLostId(lost.getId());
//        matchTrialDto.setIsDone(matchTrial.getIsDone());
//        matchTrialDto.setMatchResults(macthResultsResponse);
//
//        matchTrialRepository.save(matchTrial);
//
//        return matchTrialDto;
//    }
//
//    public MatchTrial getMatchTrial(Long matchTrialId) throws ChangeSetPersister.NotFoundException{
//        return matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
//    }

    public Map<String,String> requestToAIServer(String imageUrl, String happenDate) throws URISyntaxException, HttpServerErrorException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        System.out.println(entity);
        String aiServerURL = "http://49.50.163.148:5000/pd/?img="+imageUrl+"&happenDate="+happenDate ;
        log.info("AI로부터 실종 이미지 전송");

        ResponseEntity<String> response = restTemplate.exchange(new URI(aiServerURL), HttpMethod.GET, entity, String.class);
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

    public MatchResponseDto getMatchResponseDto(Long lostId, Collection<String> AIResponse) throws ChangeSetPersister.NotFoundException {
        List<MatchResultDto> matchResults = getLastMatchResults(lostId);
        List<MatchResultWithRescueDto> withRescue = new ArrayList<>();
        for (MatchResultDto matchResult : matchResults) {
            RescueDto rescue = rescueService.getRescue(matchResult.getDesertionNo());
            MatchResultWithRescueDto matchResultWithRescueDto =
                    MatchResultWithRescueDto.from(matchResult, rescue);
            withRescue.add(matchResultWithRescueDto);
        }
        return MatchResponseDto.from(lostId, withRescue);
    }
    @Async
    public void test(Lost lost) throws ChangeSetPersister.NotFoundException, URISyntaxException {
        Map<String, String> AIResponse = requestToAIServer(lost.getImage(), lost.getHappenDate().toString());
        MatchResponseDto response = getMatchResponseDto(lost.getId(), new ArrayList<>(AIResponse.values()));
        List<MatchResultWithRescueDto> matchResultWithRescueDtos=response.getMatchResults()
                .stream()
                .sorted(Comparator.comparing(MatchResultWithRescueDto::getRank))
                .filter(x->x.getRank()<=3)
                .collect(Collectors.toList());
        emailService.makeTemplate(lost,matchResultWithRescueDtos);
    }


    public void searchAllLosts() {
        List<Lost> findAllLosts=lostRepository.findAll();
        findAllLosts.stream()
                .filter(lost -> !lost.getImage().isEmpty())
                .forEach( lost -> {
                            try {
                                Map<String,String> aiResponse=requestToAIServer(lost.getImage(), lost.getHappenDate().toString());
                                Boolean result = createMatchResults(lost, new ArrayList<>(aiResponse.values()));
                                if (!result) {
                                    throw new BadRequestException("이미지 매칭 결과를 저장하지 못했습니다.");
                                }
                                // 메일로 보내는 로직 작성
                            } catch (URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }
}
