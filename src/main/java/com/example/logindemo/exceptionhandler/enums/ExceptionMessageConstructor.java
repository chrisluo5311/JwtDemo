package com.example.logindemo.exceptionhandler.enums;


import com.example.logindemo.common.response.MgrResponseDto;

/**
 * 建構 response 的 interface
 * */
public interface ExceptionMessageConstructor {

    MgrResponseDto getMgrResponse(Exception e);
}
