package com.example.mungtage.domain.User;

import com.example.mungtage.config.oauth.GetSocialOAuthRes;
import com.example.mungtage.config.oauth.GoogleUser;
import com.example.mungtage.config.oauth.oAuthService;
import com.example.mungtage.domain.Lost.LostRepository;
import com.example.mungtage.domain.Lost.LostService;
import com.example.mungtage.domain.Lost.dto.LostResponseDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.MatchResultRepository;
import com.example.mungtage.domain.Match.model.MatchResult;
import com.example.mungtage.domain.Rescue.RescueRepository;
import com.example.mungtage.domain.Rescue.model.Rescue;
import com.example.mungtage.domain.User.dto.GoogleResponseDto;
import com.example.mungtage.domain.User.dto.LoginResponseDto;
import com.example.mungtage.domain.User.dto.UrlRequest;
import com.example.mungtage.util.EmailMessage;
import com.example.mungtage.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {
    private  final oAuthService oAuthService;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;
    private final LostRepository lostRepository;
    private final RescueRepository rescueRepository;
    private final MatchResultRepository matchResultRepository;
    private final LostService lostService;
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

    @GetMapping("/mail/{lostid}")
    public void testMail(@PathVariable(name = "lostid") String lostId){
        Lost lost= lostRepository.findById(11L).orElse(null);
        List<Rescue> rescue1 = rescueRepository.findByDesertionNo(442420202200699L);
        List<Rescue> rescue2 = rescueRepository.findByDesertionNo(447516202200236L);
        List<Rescue> rescue3 = rescueRepository.findByDesertionNo(441374202201186L);
        Context context = new Context();
        context.setVariable("lost", lost);
        context.setVariable("rescue1", rescue1.get(0));
        context.setVariable("rescue2", rescue2.get(0));
        context.setVariable("rescue3", rescue3.get(0));
            String message = templateEngine.process("mail/emails", context);
            System.out.println(message);
            EmailMessage emailMessage = EmailMessage.builder()
                    .to("jhdl0157@naver.com")
                    .subject("[멍타주] 매칭 결과가 나왔어요")
                    .message(message)
                    .build();
            emailService.sendEmail(emailMessage);
        }
}
