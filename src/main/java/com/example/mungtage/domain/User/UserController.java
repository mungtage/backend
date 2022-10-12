package com.example.mungtage.domain.User;

import com.example.mungtage.domain.User.dto.CreateUserRequestDto;
import com.example.mungtage.domain.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
