package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
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
    public ResponseEntity<Lost> createLost(@RequestBody CreateLostRequestDto request) {
        return ResponseEntity.ok().body(lostService.createLost(request));
    }

    @GetMapping("")
    public ResponseEntity<List<Lost>> getLosts() {
        return ResponseEntity.ok().body(lostService.getLosts());
    }
}
