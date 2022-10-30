package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostService;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final LostService lostService;

    @GetMapping("")
    public ResponseEntity<MatchResponseDto> getMatchResult(@RequestParam Long lostId, Pageable pageable) {
            return ResponseEntity
                    .ok()
                    .body(matchService.getPagedMatchResultResponseDto(lostId, pageable));
    }

    @GetMapping("/test")
    public ResponseEntity<MatchResponseDto> getMachResultTest(@RequestParam Long lostId) {
        return ResponseEntity.ok().body(matchService.getMatchResponseDto(lostId));
    }

    @GetMapping("/auto")
    public void startScheduler() throws URISyntaxException {
        matchService.searchAllLosts();
    }
}
