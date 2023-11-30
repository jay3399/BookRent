package com.example.bookrent.domain.loanhistory.service;

import com.example.bookrent.application.ui.request.LoanBookRequest;
import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.book.repository.BookRepository;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import com.example.bookrent.domain.loanhistory.repository.LoanRepository;
import com.example.bookrent.domain.user.model.User;
import com.example.bookrent.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final LoanRepository loanRepository;



     public void loanBook(Long bookId ,  Long userId) throws Exception {

         Book book = bookRepository.findById(bookId).orElseThrow(
                 () -> new EntityNotFoundException("해당 엔티티를 찾을수 없음 :" + bookId)
         );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저를 찾을수 없습니다:" + userId)
        );

        if (book.isLoaned()) {
            throw new Exception("이미 대여중입니다");
        }

        if (user.isLoanLimit()) {
            throw new Exception("대출한도 초과");
        }

        book.loanTo(user);

        LoanRecord loanRecord = LoanRecord.create(user, book);

        loanRepository.save(loanRecord);

    }

    public void returnBook(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("엔티티를 찾을수 없음:" + bookId)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("엔티티를 찾을수 없음:" + userId)
        );

        book.returnBook();

        LoanRecord loanRecord = loanRepository.findByBookAndUser(book, user).orElseThrow(
                () -> new EntityNotFoundException("엔티티를 찾을수 없음:")
        );

        loanRecord.returnBook();


    }





}
