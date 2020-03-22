package com.jk.config;

import com.jk.model.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

@Configuration
public interface BookRepostory extends ElasticsearchCrudRepository<Book,String> {
}
