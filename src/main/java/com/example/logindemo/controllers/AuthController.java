package com.example.logindemo.controllers;

import com.example.logindemo.Utils.IpUtils;
import com.example.logindemo.common.response.MgrResponseDto;
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
import com.example.logindemo.payLoad.response.MessageResponse;
import com.example.logindemo.repository.RoleRepository;
import com.example.logindemo.repository.UserRepository;
import com.example.logindemo.security.jwt.JwtUtils;
import com.example.logindemo.security.services.RefreshTokenService;
import com.example.logindemo.security.services.UserDetailsImpl;
import com.example.logindemo.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登入/登出/註冊類
 *
 * @author chris
 * */
@Api(tags = "登入登出註冊")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "用户登入", httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MgrResponseDto<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                        HttpServletResponse servletResponse) {
        JwtResponse jwtResponse = loginService.loginMember(loginRequest);
        //在header中設置jwtToken
        servletResponse.setHeader(HttpHeaders.SET_COOKIE, jwtResponse.getToken());
        return MgrResponseDto.success(jwtResponse);
    }

    @ApiOperation(value = "用户註冊", httpMethod = "POST")
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public MgrResponseDto<User> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          HttpServletRequest servletRequest) {
        User user = loginService.signUp(signUpRequest,servletRequest);
        return MgrResponseDto.success(user);
    }

//    @ApiOperation(value = "用户登出", httpMethod = "GET")
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public ResponseEntity<?> logoutUser() {
//        //clear the Cookie.
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(new MessageResponse("已被登出"));
//    }


//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }

}
