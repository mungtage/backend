package com.example.mungtage.domain.User;

import com.example.mungtage.config.oauth.GetSocialOAuthRes;
import com.example.mungtage.config.oauth.GoogleUser;
import com.example.mungtage.config.oauth.oAuthService;
import com.example.mungtage.domain.User.dto.GoogleResponseDto;
import com.example.mungtage.domain.User.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {
    private  final oAuthService oAuthService;
    @GetMapping("")
    public ResponseEntity<GoogleResponseDto> getGoogleUserInfo(@RequestParam(name = "code")String code) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        GoogleResponseDto GoogleUser = oAuthService.oAuthLogin(code);
        return new ResponseEntity<>(GoogleUser, HttpStatus.OK);
    }

    @GetMapping("/google/url")
    public ResponseEntity<String> getGoogleUrl(@RequestParam(name="redirect") String url){
        String redirectUrl=oAuthService.googleUrl(url);
        return new ResponseEntity<>(redirectUrl,HttpStatus.OK);
    }
}
