package com.example.logindemo.controllers.findveiw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class FindViewController {

    @GetMapping(value = "/index")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/loginSuccess")
    public String loginSuccess(){
        return "loginSuccess";
    }

}
