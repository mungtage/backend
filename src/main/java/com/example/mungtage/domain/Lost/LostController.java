package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.dto.LostResponseDto;
import com.example.mungtage.domain.Lost.model.Lost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;

    @PostMapping("")
    public ResponseEntity<LostResponseDto> createLost(@RequestBody CreateLostRequestDto request) {
        Lost lost = lostService.createLost(request);

        return ResponseEntity.ok().body(LostResponseDto.from(lost));
    }

    @GetMapping("")
    public ResponseEntity<List<Lost>> getLosts() {
        return ResponseEntity.ok().body(lostService.getLosts());
    }
}
