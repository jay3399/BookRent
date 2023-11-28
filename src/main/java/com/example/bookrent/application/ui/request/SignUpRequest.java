package com.example.bookrent.application.ui.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @Email
    private String email;
    @NotBlank
    private String userName;




}
