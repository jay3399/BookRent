package com.example.bookrent.application.ui.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateBookRequest {


    @NotBlank
    @Size(min = 1 , max = 10)
    private String title;

    @NotBlank
    @Size(min = 1 , max = 10)
    private String author;


    @NotBlank
    private String isbn;


}
