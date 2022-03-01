package com.example.logindemo.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 項目啟動時的初始參數
 *
 * */
@Order(1)
@Component
@Slf4j
public class InitParam implements CommandLineRunner {

    /**
     * 在 Log 打印文檔與首頁地址方便測試
     *
     * */
    @Override
    public void run(String... args) throws Exception {
        log.info("[本地]swagger文檔地址: http://localhost:8090/doc.html");
        log.info("[本地]首頁地址: http://localhost:8090/");
        log.info("[正式]swagger文檔地址: [Doc](https://jwtdemo4chris.herokuapp.com/doc.html)");
        log.info("[正式]Heroku地址: https://jwtdemo4chris.herokuapp.com/)");
    }
}
