package com.example.logindemo.controllers.login;

import com.example.logindemo.common.constant.JwtConstants;
import com.example.logindemo.common.response.MgrResponseDto;
import com.example.logindemo.common.session.SessionEntity;
import com.example.logindemo.controllers.core.BaseController;
import com.example.logindemo.models.User;
import com.example.logindemo.payLoad.request.LogOutRequest;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.request.TokenRefreshRequest;
import com.example.logindemo.payLoad.response.JwtResponse;
import com.example.logindemo.payLoad.response.TokenRefreshResponse;
import com.example.logindemo.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登入/登出/註冊類
 *
 * @author chris
 * */
@Api(tags = "登入登出註冊")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "用户登入", httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MgrResponseDto<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                        HttpServletResponse servletResponse) {
        JwtResponse jwtResponse = loginService.loginMember(loginRequest);
        //在header中設置jwtToken
        servletResponse.setHeader(JwtConstants.AUTHORIZATION_CODE_KEY,JwtConstants.BEARER_CODE_KEY + jwtResponse.getToken());
        return MgrResponseDto.success(jwtResponse);
    }

    @ApiOperation(value = "用户註冊", httpMethod = "POST")
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public MgrResponseDto<User> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          HttpServletRequest servletRequest) {
        User user = loginService.signUp(signUpRequest,servletRequest);
        return MgrResponseDto.success(user);
    }

    @ApiOperation(value = "用户登出", httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public MgrResponseDto<?> logoutUser(HttpServletRequest servletRequest) {
        loginService.logOutUser(getSession(),servletRequest);
        return MgrResponseDto.success();
    }


    @ApiOperation(value = "獲得新的Token",httpMethod = "POST")
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    public MgrResponseDto<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request,
                                                             HttpServletResponse servletResponse) {
        TokenRefreshResponse tokenRefreshResponse = loginService.refreshToken(request);
        //在header中設置jwtToken
        servletResponse.setHeader(JwtConstants.AUTHORIZATION_CODE_KEY,JwtConstants.BEARER_CODE_KEY + tokenRefreshResponse.getAccessToken());
        return MgrResponseDto.success(tokenRefreshResponse);
    }

}
