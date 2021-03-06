package com.example.logindemo.service.impl;

import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.common.constant.RoleConstants;
import com.example.logindemo.common.session.SessionEntity;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.exception.user.UserException;
import com.example.logindemo.models.ERole;
import com.example.logindemo.models.RefreshToken;
import com.example.logindemo.models.Role;
import com.example.logindemo.models.User;
import com.example.logindemo.models.enums.UserStatus;
import com.example.logindemo.payLoad.request.LogOutRequest;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.request.TokenRefreshRequest;
import com.example.logindemo.payLoad.response.JwtResponse;
import com.example.logindemo.payLoad.response.TokenRefreshResponse;
import com.example.logindemo.repository.RoleRepository;
import com.example.logindemo.repository.UserRepository;
import com.example.logindemo.security.jwt.JwtUtils;
import com.example.logindemo.exception.tokenrefresh.TokenRefreshException;
import com.example.logindemo.security.services.RefreshTokenService;
import com.example.logindemo.security.services.UserDetailsImpl;
import com.example.logindemo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ??????/??????/??????service?????????
 *
 * @author chris
 * @Date 2022/02/29
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final String LOG_PREFIX = "[LoginServiceImpl]";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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

    /**
     * ??????<br>
     * jwt token ?????????1??????<br>
     * refresh token ?????????24??????
     *
     * @param loginRequest ????????????
     * @return JwtResponse
     * */
    @Override
    public JwtResponse loginMember(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        log.info("{} ??????:{} ??????????????????", LOG_PREFIX, userName);
        //?????? ??????????????????
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            log.error("?????????:{}???????????? USER_DISABLED : {}", userName, e.getMessage());
            throw new UserException(MgrResponseCode.USER_NOT_FOUND, new Object[]{userName});
        } catch (BadCredentialsException e) {
            log.error("?????????:{}???????????? INVALID_CREDENTIALS : {}", userName, e.getMessage());
            throw new UserException(MgrResponseCode.USER_PASSWORD_INVALID, new Object[]{userName});
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //??????jwtToken
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        //??????
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("{} ??????:{} ????????????:{} ????????????", LOG_PREFIX, userName, roles);
        //??????JwtResponse
        return new JwtResponse(jwtToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    /**
     * ??????<br>
     * ?????????????????????????????????????????????
     *
     * @param signUpRequest ????????????
     * @param servletRequest HttpServletRequest
     * @return User ??????
     * */
    @Transactional
    @Override
    public User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest) {
        String ip       = IpUtils.getIpAddr(servletRequest);
        String email    = signUpRequest.getEmail();
        String userName = signUpRequest.getUsername();
        log.info("{} ?????????:{} ????????????", LOG_PREFIX, userName);
        if (userRepository.existsByUsername(userName)) {
            throw new UserException(MgrResponseCode.USER_ALREADY_EXISTS, new Object[]{userName});
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserException(MgrResponseCode.USER_EMAIL_ALREADY_EXISTS, new Object[]{email});
        }

        //???????????? ??????:?????? 1:??????
        User user = User.builder()
                        .username(signUpRequest.getUsername())
                        .email(signUpRequest.getEmail())
                        .password(encoder.encode(signUpRequest.getPassword()))
                        .ip(ip)
                        .status(UserStatus.ENABLE.getCode())
                        .createTime(new Date())
                        .build();
        //??????????????????
        Set<Integer> intRoles = signUpRequest.getRole();
        //?????????????????????????????????????????????
        Set<Role> roles = new HashSet<>(Optional.ofNullable(intRoles.stream().map(r -> {
                    switch (r) {
                        case RoleConstants.ROLE_ADMIN_INT:
                            Role adminRole = roleRepository.findById(ERole.ROLE_ADMIN.getRoleId())
                                    .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_ADMIN}));
                            return adminRole;
                        case RoleConstants.ROLE_MOD_INT:
                            Role modRole = roleRepository.findById(ERole.ROLE_MODERATOR.getRoleId())
                                    .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_MODERATOR}));
                            return modRole;
                        default:
                            Role userRole = roleRepository.findById(ERole.ROLE_USER.getRoleId())
                                    .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_USER}));
                            return userRole;
                    }
                }).collect(Collectors.toSet()))
                .orElseGet(() -> {
                    Set<Role> roles2 = new HashSet<>();
                    Role userRole = roleRepository.findById(ERole.ROLE_USER.getRoleId())
                            .orElseThrow(() -> new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_USER}));
                    roles2.add(userRole);
                    return roles2;
                }));

        user.setRoles(roles);
        User userResult = userRepository.save(user);
        log.info("{} ?????????:{} ????????????", LOG_PREFIX, userResult.getUsername());
        return userResult;
    }

    /**
     * ?????????token
     *
     * @param refreshRequest Token Refresh ??????
     * @return TokenRefreshResponse
     * */
    @Transactional
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(MgrResponseCode.REFRESH_TOKEN_NOT_EXISTS_IN_DB, requestRefreshToken));
    }

    /**
     * ????????????
     *
     * @param servletRequest HttpServletRequest
     * @param sessionEntity Session
     * */
    @Override
    public void logOutUser(SessionEntity sessionEntity, HttpServletRequest servletRequest) {
        Long userId = sessionEntity.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(MgrResponseCode.USER_NOT_FOUND, new Object[]{userId}));
        refreshTokenService.deleteByUserId(user);
        String jwtToken = jwtUtils.parseJwt(servletRequest);
        redisTemplate.opsForValue().set(jwtToken, jwtToken, JwtConstants.LOGOUT_EXPIRATION_TIME, TimeUnit.HOURS);
        log.info("{} ??????:{} ??????????????????", LOG_PREFIX, user.getUsername());
    }


}
