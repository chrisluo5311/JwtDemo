package com.example.logindemo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.After;
import org.junit.Before;
import javax.annotation.Resource;

@Slf4j
public abstract class JwtDemoBaseTest {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    protected RedisTemplate<String, String> redisTemplate;

    @Before
    public void setup(){
        printDivider("開始測試");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @After
    public void afterTest(){
        printDivider("結束測試");
    }

    private void printDivider(String msg) {
        System.out.println("===================================" + msg + "===================================");
    }

}
