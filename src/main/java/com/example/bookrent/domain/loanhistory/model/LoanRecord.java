package com.example.bookrent.domain.loanhistory.model;

import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoanRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private LocalDateTime loanDate;

    @Column(nullable = true)
    private LocalDateTime returnDate;


    private LoanRecord(User user, Book book) {
        this.user = user;
        user.addLoanRecord(this);
        this.book = book;
        book.addLoanRecord(this);
        this.loanDate = LocalDateTime.now();
    }

    public void returnBook() {
        this.returnDate = LocalDateTime.now();
    }


    public static LoanRecord create(User user, Book book) {
        return new LoanRecord(user, book);
    }





}
