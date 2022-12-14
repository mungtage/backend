package com.example.mungtage.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleResponseDto {
    String userName;
    String userEmail;
    String jwtToken;
    String RefreshToken;
    String tokenType;
    public GoogleResponseDto(String userName,String userEmail, String jwtToken,String RefreshToken){
        this.userName=userName;
        this.userEmail=userEmail;
        this.jwtToken=jwtToken;
        this.RefreshToken=RefreshToken;
        this.tokenType="Auth";
    }

}
