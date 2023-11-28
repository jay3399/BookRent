package com.example.bookrent.domain.book.repository;

import com.example.bookrent.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book , Long> {

}
