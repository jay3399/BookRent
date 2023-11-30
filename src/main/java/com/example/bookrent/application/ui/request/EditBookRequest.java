package com.example.bookrent.application.ui.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EditBookRequest {


    private Long id;

    @Size(max = 10)
    private String title;

    @Size(max = 10)
    private String author;




}
