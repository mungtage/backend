package com.example.mungtage.domain;

import com.example.mungtage.domain.User.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/test")
    public String test(){
        return "hello";
    }
}
