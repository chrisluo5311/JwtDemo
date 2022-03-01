package com.example.logindemo.exceptionhandler;

import com.example.logindemo.exceptionhandler.enums.ExceptionResponseEnum;
import com.example.logindemo.common.response.MgrResponseDto;
import com.example.logindemo.service.TgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@ControllerAdvice
@Order()
public class DefaultExceptionHandler {

    @Resource
    TgService tgService;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MgrResponseDto errorHandler(Exception e) {
        e.printStackTrace();
        tgService.sendMessage(StringUtils.abbreviate(e.getMessage(),500));
        return ExceptionResponseEnum.getMgrResponseFromException(e);
    }

}
