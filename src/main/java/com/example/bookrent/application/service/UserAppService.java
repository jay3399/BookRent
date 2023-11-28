package com.example.bookrent.application.service;

import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.email.service.EmailService;
import com.example.bookrent.domain.user.service.UserService;
import com.example.bookrent.util.RedisUtil;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserService userService;
    private final EmailService emailService;

    private final RedisUtil redisUtil;

    @Transactional
    public void registerUser(SignUpRequest signUpRequest) throws Exception {
        // 중복 이메일 확인
        if (userService.checkDuplicatedEmail(signUpRequest.getEmail())) {
            throw new Exception("Email already registered.");
        }

        // 사용자 등록 (이메일 인증 상태는 미인증으로)
        userService.createUser(signUpRequest);

        String token = generateToken();
        redisUtil.setValueWithExp(token, signUpRequest.getEmail() , 30 , TimeUnit.MINUTES);

        // 인증 이메일 발송
        emailService.sendVerificationEmail(signUpRequest.getEmail() ,  token);
    }


    @Transactional
    public void verifyEmail(String token) throws Exception {

        String email = redisUtil.getValue(token);

        if (email == null) {
            throw new Exception("token is not existed");
        }

        userService.updateUserStatus(email);


    }


    private String generateToken() {
        return UUID.randomUUID().toString();
    }




}









