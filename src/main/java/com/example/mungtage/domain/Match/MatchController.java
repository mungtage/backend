package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Match.Model.MatchResult;
import com.example.mungtage.domain.Match.Model.MatchTrial;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    @GetMapping("")
    public ResponseEntity<MatchResponseDto> getMatchResult(@RequestParam String lostId) {
        MatchTrial matchTrial = matchService.createMatchTrial(Long.parseLong(lostId));

        ArrayList<Long> modelResult = new ArrayList<>();
        modelResult.add(Long.valueOf(1));
        modelResult.add(Long.valueOf(2));
        modelResult.add(Long.valueOf(3));

        for (int i=0; i<modelResult.size(); i++) {
            Boolean result = matchService.createMatchResult(matchTrial, modelResult.get(i) , i+1);
            if (!result) {
                throw new BadRequestException("이미지 매칭 결과를 저장하지 못했습니다.");
            }
        }

        MatchTrial updated = matchService.updateMatchTrialDone(matchTrial.getId());
        MatchResponseDto response = new MatchResponseDto();
        response.setId(updated.getId());
        response.setLost(updated.getLost());
        response.setMatchResults(updated.getMatchResults());
        return ResponseEntity.ok().body(response);
    }
}
