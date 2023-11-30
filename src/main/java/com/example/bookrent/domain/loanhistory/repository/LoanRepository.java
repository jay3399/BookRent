package com.example.bookrent.domain.loanhistory.repository;

import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import com.example.bookrent.domain.user.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanRecord, Long> {

    Optional<LoanRecord> findByBookAndUser(Book book, User user);


    List<LoanRecord> findByBookId(Long bookId);

}
