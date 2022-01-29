package com.example.logindemo.common.constant;

/**
 * jwt 通用常數
 *
 * @author chris
 * @Date 2022/01/29
 * */
public class JwtConstants {

    /**
     *  取 Jwt token 的 code key
     *
     * */
    public static final String AUTHORIZATION_CODE_KEY = "Authorization";

    /**
     *  取 Jwt token 的前綴
     *
     * */
    public static final String BEARER_CODE_KEY = "Bearer ";

    /**
     *  JWT 超時時放進 HttpServletRequest 的 CODE
     *
     * */
    public static final String JWT_EXPIRED_CODE_KEY = "jwtExpired";

}
