package com.jk.service;

import com.jk.mapper.BookMapper;
import com.jk.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookMapper bookMapper;


    @Override
    public void addBook(Book book) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        book.setId(uuid);
        bookMapper.addBook(book);
    }
}
