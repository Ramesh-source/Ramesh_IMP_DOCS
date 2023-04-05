package com.javaexpress.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaexpress.book.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

}
