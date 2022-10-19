package com.example.mungtage.domain.User.dto;

import com.example.mungtage.util.exception.BadRequestException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private Long id;
    private String email;
    private String userName;

    @Builder
    LoginResponseDto(Long id,String email, String userName){
        if(id==null || email.isEmpty() || userName.isEmpty()){
            throw new BadRequestException("입력값이 누락되었습니다");
        }
        this.id=id;
        this.email=email;
        this.userName=userName;
    }

}
