package com.jk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
@MapperScan("com.jk.mapper")
public class EsBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsBookApplication.class, args);
    }

}
