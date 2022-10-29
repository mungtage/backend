package com.example.mungtage.util;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.dto.MatchResultWithRescueDto;
import com.example.mungtage.domain.Rescue.model.Rescue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendSimpleMessage(String email, Long lostId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mungtagedev@gmail.com");
        message.setTo(email);

        String title = "이미지 매칭 결과가 업데이트 되었습니다.";
        message.setSubject(title);

        String text = "https://mungtage.site/api/v1/match?lostId=${Long.toString(lostId)}";
        message.setText(text);

        javaMailSender.send(message);
    }

    public void makeTemplate(Lost lost, List<MatchResultWithRescueDto> rescueList){
        Context context = new Context();
        context.setVariable("lost", lost);
        context.setVariable("MatchResultWithRescueDto1", rescueList.get(0));
        context.setVariable("MatchResultWithRescueDto2", rescueList.get(1));
        context.setVariable("MatchResultWithRescueDto3", rescueList.get(2));
        String message = templateEngine.process("mail/emails", context);
        System.out.println(message);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(lost.getUser().getEmail())
                .subject("[멍타주] 매칭 결과가 나왔어요")
                .message(message)
                .build();
        sendEmail(emailMessage);
    }

    @Async
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getTo());
        } catch (MessagingException e) {
            log.error("failed to send email", e);
            throw new RuntimeException(e);
        }
    }
}