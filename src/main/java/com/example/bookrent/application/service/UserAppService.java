package com.example.bookrent.application.service;

import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.email.service.EmailService;
import com.example.bookrent.domain.user.model.AccountStatus;
import com.example.bookrent.domain.user.model.User;
import com.example.bookrent.domain.user.service.UserService;
import com.example.bookrent.util.RedisUtil;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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


    @Transactional
    public void verifyEmail(String token) throws Exception {

        String email = validateEmail(token);

        userService.updateUserStatus(email);


    }

    public String verifyEmailForSignIn(String token) throws Exception {

        return validateEmail(token);



    }

    private String validateEmail(String token) throws Exception {
        String email = redisUtil.getValue(token);

        if (email == null) {
            throw new Exception("token is not existed");
        }
        return email;
    }


    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void removeInactiveUsers() {

        LocalDateTime date = LocalDateTime.now().minusDays(1);

        List<User> users = userService.findUsersWithExpiredVerification(date);

        for (User user : users) {
            if (user.isInactiveUser()) {
                userService.deleteUser(user);
            }
        }

    }

    public void sendSignInLink(String email) throws Exception {

        User user = userService.getUserByEmail(email);

        if (user == null || user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new Exception("유저를 찾을수 없습니다");
        }

        sendEmailVerificationForSignIn(email);


    }


    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void checkVerificationCodeDuplication(SignUpRequest signUpRequest) throws Exception {
        if (emailService.checkDuplicateRequest(signUpRequest.getEmail())) {
            throw new Exception("이미 인증링크를 보냈습니다.");
        }
    }


    private void registerNewUser(SignUpRequest signUpRequest) throws Exception {
        checkVerificationCodeDuplication(signUpRequest);
        userService.createUser(signUpRequest);
        sendEmailVerificationForSignUp(signUpRequest.getEmail());
    }

    private void checkExistingUser(SignUpRequest signUpRequest, User user) throws Exception {
        if (user.getAccountStatus() == AccountStatus.INACTIVE) {
            sendEmailVerificationForSignUp(signUpRequest.getEmail());
            throw new Exception("이메일 재인증 링크를 보냈습니다 , 확인해주세요");
        }
        throw new Exception("이미 가입된 이메일 입니다");
    }


    private void sendEmailVerificationForSignUp(String email) throws MessagingException {

        String value = "verificationForSignUp";

        sendVerificationLink(email, value);
    }

    private void sendEmailVerificationForSignIn(String email) throws MessagingException {

        String value = "verificationForSignIn";

        sendVerificationLink(email, value);


    }


    private void sendVerificationLink(String email, String value) throws MessagingException {

        String token = generateToken();

        redisUtil.setValueWithExp(token, email, 30, TimeUnit.MINUTES);

        emailService.sendVerificationEmail(email, token, value);

    }


}









