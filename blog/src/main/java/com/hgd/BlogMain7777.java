package com.hgd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.hgd.mapper")
@EnableScheduling
@EnableSwagger2
@EnableAspectJAutoProxy
public class BlogMain7777 {
    public static void main(String[] args) {
        SpringApplication.run(BlogMain7777.class);
    }
}