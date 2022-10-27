package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostService;
import com.example.mungtage.domain.Match.dto.MatchResultDto;
import com.example.mungtage.domain.Match.dto.MatchResultWithRescueDto;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import com.example.mungtage.domain.Match.model.MatchTrial;
import com.example.mungtage.domain.Match.dto.MatchTrialDto;
import com.example.mungtage.domain.Rescue.RescueService;
import com.example.mungtage.domain.Rescue.dto.RescueDto;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final LostService lostService;
    private final RescueService rescueService;

    @GetMapping("")
    public ResponseEntity<MatchResponseDto> getMatchResult(@RequestParam String lostId) throws ChangeSetPersister.NotFoundException, URISyntaxException {
        MatchTrial matchTrial = matchService.createMatchTrial(Long.parseLong(lostId));

        String lostImageURL = lostService.getLostImageURL(Long.parseLong(lostId));

        Map<Long,Long> AIResponse = matchService.requestToAIServer(lostImageURL);
        System.out.println(AIResponse);

        ArrayList<Long> modelResult = new ArrayList<>();
        modelResult.add(448548202200475L);
        modelResult.add(448548202200474L);
        modelResult.add(448548202200473L);

        Boolean result = matchService.createMatchResults(matchTrial, new ArrayList<>(AIResponse.values()));
        if (!result) {
            throw new BadRequestException("이미지 매칭 결과를 저장하지 못했습니다.");
        }

        MatchTrialDto matchTrialDto = matchService.updateMatchTrialDone(matchTrial.getId());
        List<MatchResultDto> matchResults = matchTrialDto.getMatchResults();

        List<MatchResultWithRescueDto> withRescue = new ArrayList<>();

        for (int i=0; i<matchResults.size(); i++) {
            MatchResultDto matchResult = matchResults.get(i);
            RescueDto rescue = rescueService.getRescue(matchResult.getDesertionNo());
            MatchResultWithRescueDto matchResultWithRescueDto =
                    MatchResultWithRescueDto.from(matchResult, rescue);
            withRescue.add(matchResultWithRescueDto);
        }

        MatchResponseDto response = MatchResponseDto.from(
                matchTrialDto, withRescue
        );

        return ResponseEntity.ok().body(response);
    }
}
