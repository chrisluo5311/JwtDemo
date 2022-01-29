package com.example.logindemo.exception.user;

/**
 * 用戶jwt異常類
 *
 * @author chris
 * */
public class UserJwtException extends UserException{

    public UserJwtException(String code, Object[] args,String messages) {
        super(code, args,messages);
    }
}
