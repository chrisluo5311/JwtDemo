package com.example.logindemo.security.jwt;


import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.security.services.UserDetailsImpl;
import com.example.logindemo.security.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //取 JWT from
            String jwt = parseJwt(request);
            if( jwt!=null && jwtUtils.validateJwtToken(jwt,request)){// if the request has JWT, validate it,
               String username = jwtUtils.getUserNameFromJwtToken(jwt);//parse username from it
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);//get UserDetails from username
                //create an Authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //set the current UserDetails in SecurityContext using setAuthentication(authentication) method.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            log.error("Cannot set user authentication: {}",e);
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
