package com.example.mungtage.domain.User;

import com.example.mungtage.config.oauth.UserDto;
import com.example.mungtage.domain.User.dto.CreateUserRequestDto;
import com.example.mungtage.domain.User.dto.LoginResponseDto;
import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login/google")
    public ResponseEntity<LoginResponseDto> oauthLogin(Principal principal) throws ChangeSetPersister.NotFoundException {
        String email=principal.getName();
        User currentUser=userRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new ResponseEntity<>(currentUser.toDto(), HttpStatus.OK);
    }
}
