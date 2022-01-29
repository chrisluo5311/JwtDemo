package com.example.logindemo.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用戶狀態
 *
 * @author chris
 * */
@Getter
@AllArgsConstructor
public enum UserStatus {

    ENABLE(1),
    DISABLE(-1);

    private int code;
}
