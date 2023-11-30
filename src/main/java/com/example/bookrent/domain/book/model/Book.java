package com.example.bookrent.domain.book.model;

import com.example.bookrent.application.ui.request.CreateBookRequest;
import com.example.bookrent.application.ui.request.EditBookRequest;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import com.example.bookrent.domain.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Long id;

    private String title;

    private String author;

    private LocalDateTime publishedDate;


    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus = LoanStatus.AVAILABLE;


    private Book(CreateBookRequest request) {
        this.title = request.getTitle();
        this.author = request.getAuthor();
    }

    @OneToMany(mappedBy = "book" , cascade = CascadeType.ALL)
    private List<LoanRecord> loanRecordList = new ArrayList<>();

    public void addLoanRecord(LoanRecord loanRecord) {
        loanRecordList.add(loanRecord);
    }

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateInfo(EditBookRequest request) {

        if (StringUtils.hasText(request.getAuthor())) {
            this.author = request.getAuthor();
        }

        if (StringUtils.hasText(request.getTitle())) {
            this.title = request.getTitle();
        }

    }

    public boolean isLoaned() {
        return loanStatus == LoanStatus.LOANED;
    }

    public void loanTo(User user) {
        this.loanStatus = LoanStatus.LOANED;
        this.user = user;
        user.addLoanedBook(this);
    }

    public void returnBook() {
        if (this.user != null) {
            this.user.removeLoanedBook(this);
        }
        this.loanStatus = LoanStatus.AVAILABLE;
        this.user = null;
    }





    public static Book create(CreateBookRequest request) {

        return new Book(request);

    }
}
