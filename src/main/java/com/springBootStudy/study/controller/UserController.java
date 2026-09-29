package com.springBootStudy.study.controller;


import com.springBootStudy.study.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final BookService bookService;
}
