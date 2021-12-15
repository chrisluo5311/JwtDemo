package com.example.logindemo.security.jwt;

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

@Slf4j
@Component
public class JwtUtils {

    /**
     *  JWT SECRET KEY
     * */
    @Value("${logindemo.app.jwtSecret}")
    private String jwtSecret;

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
     * 簽發JWT
     * generate a Cookie containing JWT from username, date, expiration, secret
     * */
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    /**
     * return Cookie with null value (used for clean Cookie)
     * */
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    /**
     * get username from JWT
     * */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 驗證JWT
     * validate a JWT with a secret
     * */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


    private String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))//設expiration
                .signWith(SignatureAlgorithm.HS512, jwtSecret)//簽名方式(帶密鑰
                .compact();
    }

}


