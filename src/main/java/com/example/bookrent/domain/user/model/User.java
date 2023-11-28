package com.example.bookrent.domain.user.model;


import com.example.bookrent.application.ui.request.SignUpRequest;
import com.example.bookrent.domain.loanhistory.model.LoanHistory;
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
    @Column(name = "userId")
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String username;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    private boolean emailVerifiedForSignUp = false;
    private boolean emailVerified = false;
    private LocalDateTime emailVerificationDate;

    @OneToMany(mappedBy = "user" , cascade =  CascadeType.ALL)
    private List<LoanHistory> loanHistories = new ArrayList<>();


    private User(SignUpRequest sign) {

        this.email = sign.getEmail();
        this.username = sign.getUserName();

    }
    public boolean isReauthenticate() {
        return !emailVerified || isLongTime();
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

    public void getVerificationForEmail() {
        this.emailVerified = true;
    }

    public void getEmailVerification() {
        this.emailVerifiedForSignUp = true;
    }

    public void getVerificationForSignUp() {
        this.emailVerifiedForSignUp = true;
    }




    public static User create(SignUpRequest request) {
        return new User(request);
    }


}
