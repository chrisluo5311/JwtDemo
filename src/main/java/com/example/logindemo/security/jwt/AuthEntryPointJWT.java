package com.example.logindemo.security.jwt;

import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.exception.user.UserJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ip = IpUtils.getIpAddr(request);
        log.error("未授權錯誤信息: {} 請求路徑uri: {}?{}",authException.getMessage(),request.getRequestURI(),request.getQueryString());

        //超時回應006
        if(request.getAttribute(JwtConstants.JWT_EXPIRED_CODE_KEY)!=null){
            String errorMsg = MgrResponseCode.JWT_TOKEN_EXPIRED.getMessage();
            log.error("ip:{} jwt token 已超时，需重新登入",ip);
            //response body
            final UserJwtException userJwtException = new UserJwtException(MgrResponseCode.JWT_TOKEN_EXPIRED.getCode(), new Object[]{request.getServletPath()}, errorMsg);

            objectMapper.writeValue(response.getOutputStream(), userJwtException);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //response body
        final UserJwtException userJwtException = new UserJwtException(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), new Object[]{request.getServletPath()},  authException.getMessage());

        objectMapper.writeValue(response.getOutputStream(), userJwtException);
    }
}
