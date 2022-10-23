package com.example.mungtage.domain.Rescue;

import com.example.mungtage.domain.Rescue.dto.RescueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rescue")
@RequiredArgsConstructor
public class RescueController {
    private final RescueService rescueService;

    @GetMapping("")
    public ResponseEntity<RescueDto> getResqueDetail(@RequestParam String desertionNo) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok().body(rescueService.getRescue(Long.parseLong(desertionNo)));
    }
}
