package com.example.mungtage.config.oauth;

import com.example.mungtage.config.RestTemplateConfig;
import com.example.mungtage.util.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;
    @Value("${custom.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${custom.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${custom.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${custom.google.scope}")
    private String GOOGLE_SCOPE;
    private String REDIRECT_URL;


    public String googleInitUrl(String url) {
        StringBuilder stringBuilder=new StringBuilder();
        REDIRECT_URL=url;
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", url);
        params.put("response_type", "code");
        params.put("scope", GOOGLE_SCOPE);

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));
        return stringBuilder
                .append("https://accounts.google.com/")
                .append("o/oauth2/v2/auth?")
                .append(paramStr)
                .toString();
    }

    public ResponseEntity<String> requestAccessToken(String code,String redirectUrl) {
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate=new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", redirectUrl);
        params.put("grant_type", "authorization_code");
        try {
            ResponseEntity<String> responseEntity=restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                    params,String.class);
            return responseEntity;
        }catch (RestClientException e){
            e.printStackTrace();
            throw new BadRequestException(String.format("??????????????? ????????? AccessToken??? ???????????? ???????????????. code : %s, redirectUrl : %s",code,redirectUrl));
        }
    }

    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response) throws IOException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthToken googleOAuthToken= objectMapper.readValue(response.getBody(),GoogleOAuthToken.class);
        return googleOAuthToken;

    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

        //header??? accessToken??? ?????????.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        //HttpEntity??? ?????? ????????? ????????? ????????? restTemplate?????? ????????? ???????????? ??????.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        try {
            ResponseEntity<String> response=restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);
            System.out.println("response.getBody() = " + response.getBody());
            return response;
        }catch (RestClientException e){
            throw  new BadRequestException("?????? AccessToken??? ?????? ????????? ????????? ???????????? ???????????????.");
        }
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes) throws IOException {
        GoogleUser googleUser=objectMapper.readValue(userInfoRes.getBody(),GoogleUser.class);
        return googleUser;
    }
}
