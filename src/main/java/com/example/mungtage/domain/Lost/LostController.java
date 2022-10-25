package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.dto.LostResponseDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.UserService;
import com.example.mungtage.domain.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<LostResponseDto> createLost(@RequestBody CreateLostRequestDto request, Principal principal) {
        String userEmail=principal.getName();
        Lost lost = lostService.createLost(request,userEmail);
        return ResponseEntity.ok().body(LostResponseDto.from(lost));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<ArrayList<LostResponseDto>> getLosts(Principal principal) {
        String email=principal.getName();
        User currentUser=userService.findByUserEmail(email);
        List<Lost> losts = lostService.getLosts(currentUser.getId());
        ArrayList<LostResponseDto> result = new ArrayList<>();
//        for (int i=0; i<losts.size(); i++) {
//            result.add(LostResponseDto.from(losts.get(i)));
//        }

        for(Lost lost : losts){
            result.add(LostResponseDto.from(lost));
        }
        return ResponseEntity.ok().body(result);
    }
}
