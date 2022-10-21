package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostService;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.Model.MatchTrial;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final LostService lostService;
    @GetMapping("")
    public ResponseEntity<MatchResponseDto> getMatchResult(@RequestParam String lostId) throws ChangeSetPersister.NotFoundException {
        MatchTrial matchTrial = matchService.createMatchTrial(Long.parseLong(lostId));

        String lostImageURL = lostService.getLostImageURL(Long.parseLong(lostId));

        ArrayList<Long> modelResult = new ArrayList<>();
        modelResult.add(Long.valueOf(1));
        modelResult.add(Long.valueOf(2));
        modelResult.add(Long.valueOf(3));

        Boolean result = matchService.createMatchResults(matchTrial, modelResult);
        if (!result) {
            throw new BadRequestException("이미지 매칭 결과를 저장하지 못했습니다.");
        }
        MatchResponseDto response = matchService.updateMatchTrialDone(matchTrial.getId());

        return ResponseEntity.ok().body(response);
    }
}
