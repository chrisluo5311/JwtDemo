package com.example.logindemo.service;

import com.example.logindemo.models.User;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    JwtResponse loginMember(LoginRequest loginRequest);

    User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest);

}
