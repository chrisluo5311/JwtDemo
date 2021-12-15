package com.example.logindemo.security.jwt;

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

    /**
     *  Everytime you want to get UserDetails, just use SecurityContext like this:
     *
     *  UserDetails userDetails =
     * 	(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     *
     *  userDetails.getUsername()
     *  userDetails.getPassword()
     *  userDetails.getAuthorities()
     *
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //get JWT from the HTTP Cookies
            String jwt = parseJwt(request);
            if( jwt!=null && jwtUtils.validateJwtToken(jwt)){// if the request has JWT, validate it,
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


    private String parseJwt(HttpServletRequest request){
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
