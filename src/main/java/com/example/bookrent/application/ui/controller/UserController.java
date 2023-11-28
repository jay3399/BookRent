package com.example.bookrent.application.ui.controller;

import com.example.bookrent.application.service.UserAppService;
import com.example.bookrent.application.ui.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;


    /**
     *
     * 1. 파라미터검증
     * 2. 중복 이메일 방지
     * 3. 인증링크 중복요청 방지
     * 4. 인증링크 클릭시 , 해당 링크 토큰 만료시키기 + 토큰 만료시간 정하기.
     *
     */


    @PostMapping()
    public void signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {

        userAppService.registerUser(signUpRequest);


    }

    @GetMapping("/verify")
    public void verifyEmail(@RequestParam String token) throws Exception {

        userAppService.verifyEmail(token);

    }


}
