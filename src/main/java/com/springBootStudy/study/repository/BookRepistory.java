package com.springBootStudy.study.repository;

import com.springBootStudy.study.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepistory extends JpaRepository<Book, UUID> {

}
