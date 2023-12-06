package com.example.bookrent.application.ui.controller;

import com.example.bookrent.application.service.UserAppService;
import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;

    private final JwtUtil jwtUtil;


    /**
     * 1. 파라미터검증 2. 중복 이메일 방지 3. 인증링크 중복요청 방지 4. 인증링크 클릭시 , 해당 링크 토큰 만료시키기 + 토큰 만료시간 정하기. 
     */


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {

        userAppService.registerUser(signUpRequest);

        return ResponseEntity.ok("OK");

    }


    /**
     * 로그인을 할떄마다 ,이메일 인증을 받아야하는것은 귀찮은일.
     *
     * 이메일 인증을 받은지 , 특정기간이 지나거나 아이피가 변경된 회원만 재인증메일을 보낸뒤 로그인을 완료시키고 그외는 재로그인시 이메일만 입력해도 로그인이 가능하게한다 .
     *
     */

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestParam String email) throws Exception {



        // 추후 예외 추가
        userAppService.sendSignInLink(email);

        return ResponseEntity.ok("OK");


    }


    @GetMapping("/verificationForSignUp")
    public void verifyEmailForSignUp(@RequestParam String token, HttpServletRequest request) throws Exception {

        String ipAddress = request.getRemoteAddr();

        userAppService.verifyEmailForSignUp(token, ipAddress);

    }

    @GetMapping("/verificationForSignIn")
    public ResponseEntity<?> verifyEmailForSignIn(@RequestParam String tokenForVerification, HttpServletRequest request) throws Exception {

        String ipAddress = request.getRemoteAddr();

        String email = userAppService.verifyEmailForSignIn(tokenForVerification, ipAddress);

        // 이메일 기반 토큰 생성후 반환
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(token);


    }









}
