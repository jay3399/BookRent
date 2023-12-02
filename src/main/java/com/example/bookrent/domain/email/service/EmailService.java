package com.example.bookrent.domain.email.service;


import com.example.bookrent.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final RedisUtil redisUtil;


    public void sendVerificationEmail(String userEmail, String token) throws MessagingException {

        String url = "http://localhost:8080/verify?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setTo(userEmail);
        helper.setFrom("josw90@naver.com");
        helper.setSubject("이메일인증을 완료해주세요");


        String content = "<p>환영합니다 , 이메일 인증을 완료해주세요.</p>"
                + "<a href='" + url + "' style='background-color:#4CAF50;color:white;padding:10px 20px;text-align:center;text-decoration:none;display:inline-block;font-size:16px;margin:4px 2px;cursor:pointer;border-radius:12px;'>이메일 인증하기</a>";

        helper.setText(content, true);

        mailSender.send(mimeMessage);

        preventDuplicateRequestForEmail(userEmail);


    }

    public boolean checkDuplicateRequest(String userMail) {


        return redisUtil.getValue(getKey(userMail)) != null;

    }


    /**
     * 이메일 중복요청 방지 세팅 .
     * @param userEmail
     */
    private void preventDuplicateRequestForEmail(String userEmail) {

        redisUtil.setValueWithExp(getKey(userEmail), "requested", 15, TimeUnit.MINUTES);


    }

    private static String getKey(String userEmail) {
        return "verificationLink:" + userEmail;
    }


}
