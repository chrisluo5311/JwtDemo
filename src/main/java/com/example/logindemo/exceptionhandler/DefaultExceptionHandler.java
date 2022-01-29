package com.example.logindemo.exceptionhandler;

import com.example.logindemo.exceptionhandler.enums.ExceptionResponseEnum;
import com.example.logindemo.common.response.MgrResponseDto;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order()
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MgrResponseDto errorHandler(Exception e) {
        e.printStackTrace();
        return ExceptionResponseEnum.getMgrResponseFromException(e);
    }

}
