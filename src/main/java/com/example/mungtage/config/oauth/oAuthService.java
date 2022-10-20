package com.example.mungtage.config.oauth;

import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.dto.GoogleResponseDto;
import com.example.mungtage.domain.User.model.Role;
import com.example.mungtage.domain.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
@RequiredArgsConstructor
public class oAuthService {
    private final GoogleOauthService googleOauthService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    public GoogleResponseDto oAuthLogin(String code) throws IOException {
        ResponseEntity<String> accessTokenResponse= googleOauthService.requestAccessToken(code);
        GoogleOAuthToken oAuthToken=googleOauthService.getAccessToken(accessTokenResponse);
        ResponseEntity<String> userInfoResponse=googleOauthService.requestUserInfo(oAuthToken);
        GoogleUser googleUser= googleOauthService.getUserInfo(userInfoResponse);
        User user =userRepository.findByEmail(googleUser.getEmail()).orElse(null);
        if(user==null) {
            User user1 = new User(
                    googleUser.getName(),
                    googleUser.getEmail(),
                    Role.USER);
            userRepository.save(user1);
        }
        Token token = tokenService.generateToken(googleUser.getEmail(), "ROLE_USER");
        GoogleResponseDto googleResponseDto=new GoogleResponseDto(googleUser.getName(),googleUser.getEmail(),token.getJwtToken(),token.getRefreshToken());
        return googleResponseDto;
    }
}
