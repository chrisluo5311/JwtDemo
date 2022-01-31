package com.example.logindemo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 角色類型
 * */
@Getter
@AllArgsConstructor
public enum ERole {
    ROLE_USER(1),
    ROLE_MODERATOR(2),
    ROLE_ADMIN(3);

    private Integer roleId;
}
