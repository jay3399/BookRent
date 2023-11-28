package com.example.bookrent.domain.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void sendVerificationEmail(String userEmail, String token) throws MessagingException {

        String url = "http://localhost:8080/verify?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(userEmail);
        helper.setSubject("이메일인증을 완료해주세요");


        String content = "<p>환영합니다 , 이메일 인증을 완료해주세요.</p>"
                + "<a href='" + url + "' style='background-color:#4CAF50;color:white;padding:10px 20px;text-align:center;text-decoration:none;display:inline-block;font-size:16px;margin:4px 2px;cursor:pointer;border-radius:12px;'>이메일 인증하기</a>";

        helper.setText(content, true);

        mailSender.send(mimeMessage);



    }

}
