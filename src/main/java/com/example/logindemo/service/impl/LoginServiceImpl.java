package com.example.logindemo.service.impl;

import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.common.constant.RoleConstants;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.exception.user.UserException;
import com.example.logindemo.models.ERole;
import com.example.logindemo.models.RefreshToken;
import com.example.logindemo.models.Role;
import com.example.logindemo.models.User;
import com.example.logindemo.models.enums.UserStatus;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.response.JwtResponse;
import com.example.logindemo.repository.RoleRepository;
import com.example.logindemo.repository.UserRepository;
import com.example.logindemo.security.jwt.JwtUtils;
import com.example.logindemo.security.services.RefreshTokenService;
import com.example.logindemo.security.services.UserDetailsImpl;
import com.example.logindemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登入/登出/註冊service實作類
 *
 * @author chris
 * @Date 2022/02/29
 * */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final String LOG_PREFIX = "[LoginServiceImpl]";

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    RefreshTokenService refreshTokenService;

    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Resource
    PasswordEncoder encoder;

    @Resource
    JwtUtils jwtUtils;

    @Override
    public JwtResponse loginMember(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        log.info("{} 用戶:{} 發送登入請求",LOG_PREFIX,userName);
        //驗證 用戶名與密碼
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userName, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //產生jwtToken
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        //角色
        List<String> roles = userDetails.getAuthorities().stream()
                                                         .map(item -> item.getAuthority())
                                                         .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("{} 用戶:{} 擁有角色:{} 登入成功",LOG_PREFIX,userName,roles);
        //回傳JwtResponse
        return new JwtResponse(jwtToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Transactional
    @Override
    public User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest) {
        String ip       = IpUtils.getIpAddr(servletRequest);
        String userName = signUpRequest.getUsername();
        String email    = signUpRequest.getEmail();
        log.info("{} 新用戶:{} 註冊帳號",LOG_PREFIX,userName);
        if (userRepository.existsByUsername(userName)) {
            throw new UserException(MgrResponseCode.USER_ALREADY_EXISTS,new Object[]{userName});
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserException(MgrResponseCode.USER_EMAIL_ALREADY_EXISTS,new Object[]{email});
        }

        //創建用戶 狀態:預設 1:啟用
        User user = User.builder()
                        .username(signUpRequest.getUsername())
                        .email(signUpRequest.getEmail())
                        .password(encoder.encode(signUpRequest.getPassword()))
                        .ip(ip)
                        .status(UserStatus.ENABLE.getCode())
                        .createTime(new Date())
                        .build();
        //取的註冊腳色
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role>   roles    = new HashSet<>();

        //未輸入權限一律預設為一般使用者
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND,new Object[]{ERole.ROLE_USER}));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case RoleConstants.ROLE_ADMIN:
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND,new Object[]{ERole.ROLE_ADMIN}));
                        roles.add(adminRole);

                        break;
                    case RoleConstants.ROLE_MOD:
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND,new Object[]{ERole.ROLE_MODERATOR}));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND,new Object[]{ERole.ROLE_USER}));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        User userResult = userRepository.save(user);
        log.info("{} 新用戶:{} 註冊成功",LOG_PREFIX,userResult.toString());
        return userResult;
    }
}