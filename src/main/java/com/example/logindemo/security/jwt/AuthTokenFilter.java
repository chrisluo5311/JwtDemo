package com.example.logindemo.security.jwt;


import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.Utils.SessionUtils;
import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.common.constant.SessionConstants;
import com.example.logindemo.common.session.SessionEntity;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.exception.user.UserException;
import com.example.logindemo.security.services.UserDetailsImpl;
import com.example.logindemo.security.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ip = IpUtils.getIpAddr(request);
        log.info("從 ip {}:發送請求uri: {}",ip, request.getRequestURI());

        try{
            //取 JWT
            String jwt = parseJwt(request);
            if( jwt!=null){
                String userName = jwtUtils.getUserNameFromJwtToken(jwt);
                //查看 redis 登出黑名單
                if(redisTemplate.hasKey(jwt)){
                    throw new UserException(MgrResponseCode.USER_ALREADY_LOGOUT,new Object[]{userName});
                }
                if(jwtUtils.validateJwtToken(jwt,request)){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    SessionEntity sessionEntity = SessionEntity.builder()
                                                               .userName(userName)
                                                               .ip(ip)
                                                               .build();
                    //request header中設置session
                    SessionUtils.pushSessionToRequest(sessionEntity,request);
                }
            }
        }catch (Exception e){
            log.error("無法設置用戶權限: {}",e);
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 从 HttpServletRequest的Header取Authorization的值<br>
     * 并截断Bearer 字段只取后方的token
     * @param request HttpServletRequest
     * @return String jwt token
     * */
    private String parseJwt(HttpServletRequest request){
        String jwtToken = null;

        final String requestTokenHeader = request.getHeader(JwtConstants.AUTHORIZATION_CODE_KEY);

        // JWT Token在"Bearer token"里 移除Bearer字段只取Token
        if(requestTokenHeader!=null){
            if (requestTokenHeader.startsWith(JwtConstants.BEARER_CODE_KEY)) {
                jwtToken = requestTokenHeader.substring(7);
            } else {
                logger.warn("JWT Token 不在Bearer里面");
            }
        }
        return jwtToken;
    }
}
