package com.jk.mapper;

import com.jk.model.Book;

public interface BookMapper {


    /*@Insert("insert into t_book(id,bname,price,btype,bjianjie)  values(#{id},#{bname},#{price},#{btype},#{bjianjie})")*/
    void addBook(Book book);
}
