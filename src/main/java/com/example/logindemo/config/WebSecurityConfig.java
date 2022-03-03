package com.example.logindemo.config;

import com.example.logindemo.controllers.logout.LogoutHandler;
import com.example.logindemo.security.jwt.AuthEntryPointJWT;
import com.example.logindemo.security.jwt.AuthTokenFilter;
import com.example.logindemo.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity //allows Spring to find and automatically apply the class to the global Web Security.
@EnableGlobalMethodSecurity(prePostEnabled = true) //provides AOP security on methods. It enables @PreAuthorize, @PostAuthorize
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Resource
    private AuthEntryPointJWT authEntryPointJWT;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //設置腳色定義
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Resource
    public LogoutHandler logoutHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                                    "/configuration/ui",
                                    "/swagger-resources/**",
                                    "/configuration/security",
                                    "/swagger-ui.html",
                                    "/doc.html",
                                    "/webjars/**",
                                    "/favicon.ico",
                                    "/error",
                                    "/mode-Text.js",
                                    "/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJWT).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()//定義哪些url需要被保護
                .antMatchers("/",
                        "/login",
                        "/index",
                        "/api/auth/**",
                        "/tg/receive",
                        "/inner/**",
                        "/websocket/**").permitAll()
                .anyRequest().authenticated().and()
                .logout()
                .logoutUrl("/api/auth/userLogout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessUrl("/index");
        //加filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
