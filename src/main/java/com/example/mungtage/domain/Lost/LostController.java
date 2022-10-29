package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.dto.LostResponseDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.MatchService;
import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.UserService;
import com.example.mungtage.domain.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;
    private final UserService userService;
    private final MatchService matchService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<LostResponseDto> createLost(@RequestBody CreateLostRequestDto request, Principal principal) throws URISyntaxException {
        String userEmail=principal.getName();
        Lost lost = lostService.createLost(request,userEmail);
        //저장된 lost 이미지를 ai서버로 보내기
        matchService.requestToAIServer(lost.getImage());
        return ResponseEntity.ok().body(LostResponseDto.from(lost));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<ArrayList<LostResponseDto>> getLosts(Principal principal) {
        String email=principal.getName();
        User currentUser=userService.findByUserEmail(email);
        List<Lost> losts = lostService.getLosts(currentUser.getId());
        ArrayList<LostResponseDto> result = new ArrayList<>();
        for(Lost lost : losts){
            result.add(LostResponseDto.from(lost));
        }
        return ResponseEntity.ok().body(result);
    }
}
