package com.example.logindemo.exception.base;

import com.example.logindemo.exception.Modules;
import lombok.ToString;

/**
 *  基礎異常
 *
 *  @author chris
 * */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 所屬模塊
     */
    private Modules module;

    /**
     * 錯誤碼
     */
    private String code;

    /**
     * 錯誤碼對應參數
     */
    private Object[] args;

    /**
     * 錯誤消息
     */
    private String message;

    public BaseException(Modules module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.message = defaultMessage;
    }

    public BaseException(Modules module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BaseException(Modules module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    public Modules getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMessage() {
        return message;
    }

}
