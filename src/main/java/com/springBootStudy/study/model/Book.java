package com.springBootStudy.study.model;

import com.springBootStudy.study.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "book_name", nullable = false, length = 200)
    private String bookName;

    @Column(name = "book_author", nullable = false, length = 200)
    private String bookAuthor;

    @Column(name = "book_isbn", nullable = false, length = 13)
    private String bookIsbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status", nullable = false, length = 20)
    private BookStatus bookStatus;
}
