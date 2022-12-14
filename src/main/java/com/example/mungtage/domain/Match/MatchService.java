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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MatchService {
    private final ObjectMapper objectMapper;
    private final MatchResultRepository matchResultRepository;
    private final RescueService rescueService;
    private final EmailService emailService;
    private final LostRepository lostRepository;

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
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(asc("rank")));
        System.out.println(pageRequest);
        List<MatchResult> result = matchResultRepository.findByLostId(lostId, pageRequest);
        List<MatchResultDto> dto = result
                .stream()
                .map(s -> MatchResultDto.from(s))
                .collect(Collectors.toList());

        return dto;
    }

    public Map<String,String> requestToAIServer(String imageUrl, String happenDate) throws URISyntaxException, HttpServerErrorException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        String aiServerURL = "http://49.50.163.148:5000/pd/?img="+imageUrl+"&happenDate="+happenDate ;
        System.out.println(aiServerURL);
        log.info("AI????????? ?????? ????????? ??????");

        ResponseEntity<String> response = restTemplate.exchange(new URI(aiServerURL), HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("????????? ??????!! {}",response.getBody());
            try {
                Map<String, String> map = objectMapper.readValue(response.getBody(), Map.class);
                log.info("Json to Map ?????? ??????!! {}",map);
                return map;
            } catch (IOException e) {
                throw new BadRequestException("?????? ??????!!!");
            }
        } else {
            throw new HttpServerErrorException(response.getStatusCode(), "AI server request error");
        }
    }

    public MatchResponseDto getPagedMatchResultResponseDto(Long lostId, Pageable pageable) throws BadRequestException {
        try {
            Page<MatchResult> matchResultPage = matchResultRepository.findByLostIdOrderByUpdatedDateAsc(lostId, pageable);
            List<MatchResultDto> matchResults = matchResultPage
                    .stream()
                    .map(s -> MatchResultDto.from(s))
                    .collect(Collectors.toList());
            List<MatchResultWithRescueDto> withRescue = new ArrayList<>();
            for (MatchResultDto matchResult : matchResults) {
                RescueDto rescue = rescueService.getRescue(matchResult.getDesertionNo());
                System.out.println(rescue);
                MatchResultWithRescueDto matchResultWithRescueDto =
                        MatchResultWithRescueDto.from(matchResult, rescue);
                withRescue.add(matchResultWithRescueDto);
            }
            return MatchResponseDto.from(lostId, withRescue);
        } catch (Error e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public MatchResponseDto getMatchResponseDto(Long lostId) {
        List<MatchResultDto> matchResults = getLastMatchResults(lostId);
        List<MatchResultWithRescueDto> withRescue = new ArrayList<>();
        for (MatchResultDto matchResult : matchResults) {
            RescueDto rescue = rescueService.getRescue(matchResult.getDesertionNo());
            System.out.println(rescue);
            MatchResultWithRescueDto matchResultWithRescueDto =
                    MatchResultWithRescueDto.from(matchResult, rescue);
            withRescue.add(matchResultWithRescueDto);
            System.out.println(withRescue);
        }
        return MatchResponseDto.from(lostId, withRescue);
    }
    @Async
    public void backToAiServerAndEmailSend(Lost lost) throws URISyntaxException {
        Map<String, String> AIResponse = requestToAIServer(lost.getImage(), lost.getHappenDate().toString().replace("-", ""));
        matchResultRepository.deleteByLostId(lost.getId());
        Boolean isWellCreated = createMatchResults(lost, new ArrayList<>(AIResponse.values()));
        if (!isWellCreated) {
            throw new BadRequestException("????????? ?????? ????????? ???????????? ???????????????.");
        }
        MatchResponseDto response = getMatchResponseDto(lost.getId());
        emailService.makeTemplate(lost, response.getMatchResults());
    }


    public void searchAllLosts() throws URISyntaxException {
        List<Lost> findAllLosts=lostRepository.findAll();
        log.info("Lost??? ?????? : {}",findAllLosts.size());
        for(Lost lost:findAllLosts){
            log.info("Lost id = {} ??? Ai????????? ????????? ??????????????????",lost.getId());
            backToAiServerAndEmailSend(lost);
        }
    }
}
