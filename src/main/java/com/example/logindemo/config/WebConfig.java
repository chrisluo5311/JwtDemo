package com.example.logindemo.config;

import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    UserRepository userRepository;


    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry
                .addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        if (request.getRequestURI().startsWith("/error")) {
                            return false;
                        }
                        log.info(" IP來源:{}: 請求路徑:{}?{}",IpUtils.getIpAddr(request),
                                                            request.getRequestURI(),
                                                            request.getQueryString());

                        return true;
                    }
                })
                // -- swagger start --
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-resources")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/favicon.ico")
                // -- swagger end --
                .excludePathPatterns("/api/auth/**")
                .excludePathPatterns("/api/test/**")
                .excludePathPatterns("/members")
                .addPathPatterns("/**");
    }

}
