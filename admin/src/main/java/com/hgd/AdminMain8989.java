package com.hgd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.hgd.mapper")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminMain8989 {
    public static void main(String[] args) {
        SpringApplication.run(AdminMain8989.class);
    }
}