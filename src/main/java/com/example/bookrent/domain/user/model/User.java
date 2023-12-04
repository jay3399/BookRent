package com.example.bookrent.domain.user.model;


import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String username;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.INACTIVE;
    private boolean emailVerified = false;
    private LocalDateTime emailVerificationDate;
    private LocalDateTime createAt = LocalDateTime.now();

    private Long loanLimit = 10L;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LoanRecord> loanRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    public void addLoanedBook(Book book) {
        books.add(book);
    }

    public void removeLoanedBook(Book book) {
        books.remove(book);

    }
    public void addLoanRecord(LoanRecord loanRecord) {
        loanRecords.add(loanRecord);
    }

    private User(SignUpRequest sign) {

        this.email = sign.getEmail();
        this.username = sign.getUserName();

    }
    public boolean isReauthenticate() {
        return !emailVerified || isLongTime();
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    private static final long VERIFICATION_EXPIRATION_HOURS = 24;


    public boolean isLongTime() {

        if (emailVerificationDate != null) {

            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(emailVerificationDate, now);

            long diffHours = duration.toHours();

            return diffHours > VERIFICATION_EXPIRATION_HOURS;
        }

        return false;
    }

    public boolean isLoanLimit() {
        return books.size() >= loanLimit;
    }

    public void getVerificationForEmail() {
        this.emailVerified = true;
    }


    public void getVerificationForSignUp() {
        this.accountStatus = AccountStatus.ACTIVE;
    }


    public boolean isInactiveUser() {
        return accountStatus == AccountStatus.INACTIVE;
    }


    public static User create(SignUpRequest request) {
        return new User(request);
    }


}
