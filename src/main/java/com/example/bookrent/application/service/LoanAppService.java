package com.example.bookrent.application.service;

import com.example.bookrent.application.ui.request.LoanBookRequest;
import com.example.bookrent.application.ui.request.ReturnBookRequest;
import com.example.bookrent.domain.loanhistory.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoanAppService {

    private final LoanService loanService;


    @Transactional
    public void loanBook(LoanBookRequest request) throws Exception {
        loanService.loanBook(request.getBookId(), request.getUserId());
    }


    @Transactional
    public void returnBook(ReturnBookRequest request) {
        loanService.returnBook(request.getBookId(), request.getUserId());
    }


}
