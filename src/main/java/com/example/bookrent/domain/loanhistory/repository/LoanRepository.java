package com.example.bookrent.domain.loanhistory.repository;

import com.example.bookrent.domain.loanhistory.model.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanHistory , Long> {

}
