package com.example.bookrent.application.service;

import com.example.bookrent.application.ui.request.CreateBookRequest;
import com.example.bookrent.application.ui.request.EditBookRequest;
import com.example.bookrent.domain.book.model.Book;
import com.example.bookrent.domain.book.service.BookService;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookAppService {

    private final BookService bookService;



    @Transactional
    public void saveBook(CreateBookRequest request) {
        bookService.saveBook(Book.create(request.getTitle() , request.getAuthor()));
    }

    @Transactional
    public void editBook(EditBookRequest request) {
        bookService.updateBook(request);
    }


    @Transactional
    public List<LoanRecord> getLoanRecordsByBook(Long bookId) {

        return bookService.getLoanRecordByBook(bookId);

    }




}
