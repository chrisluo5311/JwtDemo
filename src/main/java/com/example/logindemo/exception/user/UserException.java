package com.example.logindemo.exception.user;


import com.example.logindemo.exception.Modules;
import com.example.logindemo.exception.base.BaseException;
import com.example.logindemo.exception.responsecode.MgrResponseCode;

/**
 * 用户信息異常類
 *
 * @author chris
 */
public class UserException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UserException(MgrResponseCode code, Object[] args) {
        super(Modules.USER, code, args, code.getMessage());
    }

    public UserException(String code,Object[] args,String messages){
        super(Modules.USER,code,args,messages);
    }
}
