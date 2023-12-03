package com.example.bookrent.application.service;

import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.email.service.EmailService;
import com.example.bookrent.domain.user.model.AccountStatus;
import com.example.bookrent.domain.user.model.User;
import com.example.bookrent.domain.user.service.UserService;
import com.example.bookrent.util.RedisUtil;
import jakarta.mail.MessagingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
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

        User user = userService.getUserByEmail(signUpRequest.getEmail());

        if (user != null) {
            checkExistingUser(signUpRequest, user);

        } else {
            registerNewUser(signUpRequest);
        }

    }

    private void registerNewUser(SignUpRequest signUpRequest) throws Exception {
        checkVerificationCodeDuplication(signUpRequest);
        userService.createUser(signUpRequest);
        sendEmailVerification(signUpRequest);
    }

    private void checkExistingUser(SignUpRequest signUpRequest, User user) throws Exception {
        if (user.getAccountStatus() == AccountStatus.INACTIVE) {
            sendEmailVerification(signUpRequest);
            throw new Exception("이메일 재인증 링크를 보냈습니다 , 확인해주세요");
        }
        throw new Exception("이미 가입된 이메일 입니다");
    }

    private void sendEmailVerification(SignUpRequest signUpRequest) throws MessagingException {
        String token = generateToken();

        redisUtil.setValueWithExp(token, signUpRequest.getEmail(), 30, TimeUnit.MINUTES);

        // 인증 이메일 발송
        emailService.sendVerificationEmail(signUpRequest.getEmail(), token);
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

    private void checkVerificationCodeDuplication(SignUpRequest signUpRequest) throws Exception {
        if (emailService.checkDuplicateRequest(signUpRequest.getEmail())) {
            throw new Exception("이미 인증링크를 보냈습니다.");
        }
    }


}









