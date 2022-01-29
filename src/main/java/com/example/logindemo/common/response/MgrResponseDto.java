package com.example.logindemo.common.response;


import com.example.logindemo.exception.responsecode.MgrResponseCode;

public class MgrResponseDto<T> {

    private String code;

    private String message;

    private T data;

    public static <T> MgrResponseDto<T> success(T data) {
        MgrResponseDto<T> dto = new MgrResponseDto<>();
        dto.setCode(MgrResponseCode.SUCCESS.getCode());
        dto.setData(data);
        return dto;
    }

    public static <T> MgrResponseDto<T> error(MgrResponseCode code) {
        MgrResponseDto<T> dto = new MgrResponseDto<>();
        dto.setCode(code.getCode());
        dto.setMessage(code.getMessage());
        return dto;
    }

    public static MgrResponseDto<Void> error(String code, String message) {
        MgrResponseDto<Void> dto = new MgrResponseDto<Void>();
        dto.setCode(code);
        dto.setMessage(message);
        return dto;
    }

    public static MgrResponseDto<Void> success() {
        return success(null);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode(MgrResponseCode code) {
        this.code = code.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
