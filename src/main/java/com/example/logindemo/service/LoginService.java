package com.example.logindemo.service;

import com.example.logindemo.common.session.SessionEntity;
import com.example.logindemo.models.User;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.request.TokenRefreshRequest;
import com.example.logindemo.payLoad.response.JwtResponse;
import com.example.logindemo.payLoad.response.TokenRefreshResponse;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    JwtResponse loginMember(LoginRequest loginRequest);

    User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest);

    TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest);

    void logOutUser(SessionEntity sessionEntity, HttpServletRequest servletRequest);
}
