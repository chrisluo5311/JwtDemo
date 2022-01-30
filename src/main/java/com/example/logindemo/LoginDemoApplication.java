package com.example.logindemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 * swagger文檔地址: http://localhost:8090/doc.html
 * 首頁地址: http://localhost:8090/
 *
 * */
@PropertySource("classpath:system-${spring.profiles.active}.properties")
@SpringBootApplication
public class LoginDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginDemoApplication.class, args);
    }




}
