package com.example.mungtage.util;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String email, Long lostId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mungtagedev@gmail.com");
        message.setTo(email);

        String title = "이미지 매칭 결과가 업데이트 되었습니다.";
        message.setSubject(title);

        String text = "https://mungtage.site/api/v1/match?lostId=${Long.toString(lostId)}";
        message.setText(text);

        emailSender.send(message);
    }
}