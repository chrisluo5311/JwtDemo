package com.example.logindemo.exception.user;

import com.example.logindemo.exception.responsecode.MgrResponseCode;

/**
 * 用戶jwt異常類
 *
 * @author chris
 * */
public class UserJwtException extends UserException{

    public UserJwtException(String code,Object[] args,String messages){
        super(code,args,messages);
    }

    public UserJwtException(MgrResponseCode code, Object[] args) {
        super(code, args);
    }


}
