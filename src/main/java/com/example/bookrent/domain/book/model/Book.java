package com.example.bookrent.domain.book.model;

import com.example.bookrent.domain.loanhistory.model.LoanHistory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Long id;

    private String title;

    private String author;

    private LocalDateTime publishedDate;


    @OneToMany(mappedBy = "book" , cascade = CascadeType.ALL)
    private List<LoanHistory> loanHistoryList = new ArrayList<>();



}
