package com.example.logindemo.Utils;

import com.example.logindemo.common.constant.SessionConstants;
import com.example.logindemo.common.session.SessionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SessionUtils {

    private static ObjectMapper objectMapper;

    @Autowired
    public SessionUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static void pushSessionToRequest(SessionEntity entity, ServletRequest request) {
        String encodedSession = encodeSessionEntity(entity);
        request.setAttribute(SessionConstants.SESSION_ATTRIBUTE_KEY, encodedSession);
    }

    private static String encodeSessionEntity(SessionEntity sessionEntity) {
        try {
            String json = objectMapper.writeValueAsString(sessionEntity);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
