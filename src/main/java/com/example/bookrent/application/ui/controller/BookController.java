package com.example.bookrent.application.ui.controller;

import com.example.bookrent.application.service.BookAppService;
import com.example.bookrent.application.ui.request.CreateBookRequest;
//import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import com.example.bookrent.domain.loanhistory.model.LoanRecord;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookAppService bookAppService;


    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody CreateBookRequest request) {

        bookAppService.saveBook(request);

        return ResponseEntity.ok("완");


    }

    @GetMapping("/books/{bookId}/loanRecords")
    public ResponseEntity<List<LoanRecord>> getLoanRecords(@PathVariable Long bookId) {

        List<LoanRecord> loanRecordsByBook = bookAppService.getLoanRecordsByBook(bookId);

        return ResponseEntity.ok(loanRecordsByBook);

    }


}
