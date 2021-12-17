package com.example.logindemo.controllers;

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

}
