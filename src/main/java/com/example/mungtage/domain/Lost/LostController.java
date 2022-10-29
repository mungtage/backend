package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.dto.LostResponseDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.MatchService;
import com.example.mungtage.domain.Match.dto.MatchResponseDto;
import com.example.mungtage.domain.Match.dto.MatchResultWithRescueDto;
import com.example.mungtage.domain.Match.model.MatchTrial;
import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.UserService;
import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lost")
@RequiredArgsConstructor
public class LostController {
    private final LostService lostService;
    private final UserService userService;
    private final MatchService matchService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<LostResponseDto> createLost(@RequestBody CreateLostRequestDto request, Principal principal) throws URISyntaxException, ChangeSetPersister.NotFoundException {
        String userEmail=principal.getName();
        Lost lost = lostService.createLost(request,userEmail);
        matchService.test(lost);
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

    @DeleteMapping("/{lostId}")
    public ResponseEntity<Boolean> deleteLost(@PathVariable(name = "lostId")Long lostId,Principal principal){
        String email=principal.getName();
        User currentUser=userService.findByUserEmail(email);
        return new ResponseEntity<>(lostService.deleteLostId(lostId,currentUser.getId()), HttpStatus.OK);
    }
}
