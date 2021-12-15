package com.example.logindemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:system-${spring.profiles.active}.properties")
@SpringBootApplication
public class LoginDemoApplication {



    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(LoginDemoApplication.class, args);

//        SystemProperties systemProperties = ctx.getBean(SystemProperties.class);
//        System.out.println(systemProperties.getName());
//        System.out.println(systemProperties.getLogintype());
//        System.out.println(systemProperties.getPort());
    }

}
