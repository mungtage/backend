package com.example.mungtage.domain.User;

import com.example.mungtage.config.oauth.GetSocialOAuthRes;
import com.example.mungtage.config.oauth.GoogleUser;
import com.example.mungtage.config.oauth.oAuthService;
import com.example.mungtage.domain.User.dto.GoogleResponseDto;
import com.example.mungtage.domain.User.dto.LoginResponseDto;
import com.example.mungtage.domain.User.dto.UrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {
    private  final oAuthService oAuthService;
    @PostMapping("")
    public ResponseEntity<GoogleResponseDto> getGoogleUserInfo(@RequestBody @Valid UrlRequest urlRequest) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ urlRequest.getCode());
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 url :"+ urlRequest.getRedirectUrl());
        GoogleResponseDto GoogleUser = oAuthService.oAuthLogin(urlRequest.getCode(),urlRequest.getRedirectUrl());
        return new ResponseEntity<>(GoogleUser, HttpStatus.OK);
    }

    @GetMapping("/google/url")
    public void getGoogleUrl(@RequestParam String url, HttpServletResponse response) throws IOException {
        System.out.println("asdasdasd"+url);
        String redirectUrl=oAuthService.googleUrl(url);
        response.sendRedirect(redirectUrl);
    }
}
