package com.example.logindemo.security.jwt;

import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import io.jsonwebtoken.*;

/**
 * jwt 工具類
 *
 * @author chris
 * @Date 2022/01/29
 * */
@Slf4j
@Component
public class JwtUtils {

    /**
     *  JWT 密鑰
     * */
    @Value("${logindemo.app.jwtSecret}")
    private String jwtSecret;

    /**
     *  jwt token 超時時間: 一小時
     * */
    @Value("${logindemo.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${logindemo.app.jwtCookieName}")
    private String jwtCookie;

    /**
     * get JWT from Cookies by Cookie name
     * */
    public String getJwtFromCookies(HttpServletRequest request) {
        //getCookie判斷HttpServletRequest的cookie name跟我們設置的是否相同;是的話回傳cookie
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 產生jwt token
     *
     * @param userPrincipal 用戶資訊
     * @return jwt token
     * */
    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * 透過用戶名自產生jwt token
     *
     * @param username 用戶名
     * @return jwt token
     * */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))//設expiration
                   .signWith(SignatureAlgorithm.HS512, jwtSecret)//簽名方式(帶密鑰
                   .compact();
    }

    /**
     * return Cookie with null value (used for clean Cookie)
     * */
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    /**
     * 從 jwt token 中取出用戶名
     *
     * @param token jwt token
     * @return 用戶名
     * */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 驗證JWT
     *
     * @param authToken jwt token
     * @param servletRequest HttpServletRequest
     * */
    public boolean validateJwtToken(String authToken,HttpServletRequest servletRequest) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("無效的 JWT 簽名: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("無效的 JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token 超時: {}", e.getMessage());
            servletRequest.setAttribute(JwtConstants.JWT_EXPIRED_CODE_KEY ,e.getCause());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token 不支持: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string 為空: {}", e.getMessage());
        }

        return false;
    }

}


