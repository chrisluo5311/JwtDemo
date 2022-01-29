package com.example.logindemo.exception.responsecode;

import lombok.AllArgsConstructor;

/**
 * 錯誤響應碼與信息對照表
 *
 * */
@AllArgsConstructor
public enum MgrResponseCode {

    SUCCESS("0000", "成功"),

    INVALID_TOKEN("0001", "無效Token"),
    INVALID_CAPTCHA("0002", "驗證碼錯誤"),
    INVALID_REQUEST("0003", "請求錯誤"),
    INVALID_REMOTE_API("0004", "遠程調用異常"),
    CAPTCHA_EXPIRE_OR_NOT_EXIST("0005", "驗證碼超時或不存在，請重新產生"),
    JWT_TOKEN_EXPIRED("0006","Jwt Token 已超時，請重新登入"),

    PARAM_NOT_FOUND("0101", "參數不存在"),
    PARAM_INVALID("0102", "無效的參數"),

    // 未知错误
    UNKNOWN_ERROR("9999", "系统错误");

    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
