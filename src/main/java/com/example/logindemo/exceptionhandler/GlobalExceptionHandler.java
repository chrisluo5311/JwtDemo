package com.example.logindemo.exceptionhandler;

import com.example.logindemo.Utils.TimeUtil;
import com.example.logindemo.exception.base.BaseException;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.common.response.MgrResponseDto;
import com.example.logindemo.exception.tokenrefresh.TokenRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 處理 Exception 類
 *
 * @author chris
 * */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public MgrResponseDto errorHandler(BaseException e) {
        e.printStackTrace();

        MgrResponseDto dto = new MgrResponseDto();

        dto.setCode(e.getCode());
        dto.setMessage(e.getMessage());

        return dto;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MgrResponseDto<?> errorHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();

        MgrResponseDto<?> dto = new MgrResponseDto();

        List<FieldError> errorList = e.getBindingResult().getFieldErrors();
        //并接错误信习
        String message = errorList.size() + " 項錯誤: ";
        message += errorList.stream()
                .map(error -> "參數:'" + error.getField() + "' " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        dto.setCode(MgrResponseCode.PARAM_INVALID);
        dto.setMessage(message);

        return dto;
    }

    @ExceptionHandler(BindException.class)
    public MgrResponseDto<?> errorHandler(BindException e) {
        e.printStackTrace();

        StringBuilder sb = new StringBuilder();
        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                Object rejectedValue = fieldError.getRejectedValue();
                if(rejectedValue instanceof Date){
                    String illegalDate = TimeUtil.customDateToString((Date) fieldError.getRejectedValue(),"YYYY-MM-DD HH:mm:ss");
                    sb.append(fieldError.getDefaultMessage()).append(" ").append(illegalDate).append(" ; ");
                } else {
                    if(rejectedValue!=null){
                        sb.append(fieldError.getDefaultMessage()).append(" ").append(rejectedValue).append("; ");
                    } else {
                        sb.append(fieldError.getDefaultMessage()).append("; ");
                    }
                }
            });
        }
        MgrResponseDto<?> dto = new MgrResponseDto();
        dto.setCode(MgrResponseCode.PARAM_INVALID);
        dto.setMessage(sb.toString());
        return dto;
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MgrResponseDto handleTokenRefreshException(TokenRefreshException ex) {
        return MgrResponseDto.error(String.valueOf(HttpStatus.FORBIDDEN.value()),ex.getMessage());
    }

}
