package com.example.bookrent.domain.book.service;

import com.example.bookrent.application.ui.request.EditBookRequest;
import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.book.repository.BookRepository;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import com.example.bookrent.domain.loanhistory.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;


    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(EditBookRequest request) {

        Book book = bookRepository.findById(request.getId()).orElseThrow(
                () -> new EntityNotFoundException("엔티티를 찾을수 없습니다")
        );

        book.updateInfo(request);

    }

    public List<LoanRecord> getLoanRecordByBook(Long bookId) {
        return loanRepository.findByBookId(bookId);
    }


}
