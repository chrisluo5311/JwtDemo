package com.example.logindemo.logintest;

import com.example.logindemo.JwtDemoBaseTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class LoginTest extends JwtDemoBaseTest {

    /** 登入验证码 redis key 前缀 */
    public static final String CAPTCHA_PREFIX = "loginCaptcha";

    @Resource
    PasswordEncoder passwordEncoder;

//    @Test
    public void login_test() throws Exception {

    }

//    @Test
    public void encrypt_test() {
        System.out.println("PasswordEncoder 測試");
        String pwd = "11111111";
        System.out.println("密碼: 11111111 加密後: " + passwordEncoder.encode(pwd));
    }

}