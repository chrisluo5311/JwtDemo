package com.example.logindemo.security;

import com.example.logindemo.security.jwt.AuthEntryPointJWT;
import com.example.logindemo.security.jwt.AuthTokenFilter;
import com.example.logindemo.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    /**
     * It tells Spring Security how we configure CORS and CSRF,
     * when we want to require all users to be authenticated or not,
     * which filter (AuthTokenFilter) and when we want it to work (filter before UsernamePasswordAuthenticationFilter),
     * which Exception Handler is chosen (AuthEntryPointJwt).
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJWT).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()//定義哪些url需要被保護
                .antMatchers("/index").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/api/auth/**").permitAll() // 定義匹配到"/api/auth/**" 不需要驗證
                .antMatchers("/api/test/**").permitAll() // 定義匹配到"/api/test/**" 不需要驗證
                .anyRequest().authenticated(); // 其他尚未匹配到的url都需要身份驗
        //加filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
