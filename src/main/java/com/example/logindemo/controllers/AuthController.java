package com.example.logindemo.controllers;

import com.example.logindemo.common.response.MgrResponseDto;
import com.example.logindemo.models.ERole;
import com.example.logindemo.models.Role;
import com.example.logindemo.models.User;
import com.example.logindemo.payLoad.request.LoginRequest;
import com.example.logindemo.payLoad.request.SignupRequest;
import com.example.logindemo.payLoad.response.MessageResponse;
import com.example.logindemo.payLoad.response.UserInfoResponse;
import com.example.logindemo.repository.RoleRepository;
import com.example.logindemo.repository.UserRepository;
import com.example.logindemo.security.jwt.JwtUtils;
import com.example.logindemo.security.services.UserDetailsImpl;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    AuthenticationManager authenticationManager;

    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Resource
    PasswordEncoder encoder;

    @Resource
    JwtUtils jwtUtils;

    @ApiOperation(value = "用户登入", httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MgrResponseDto<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpServletResponse servletResponse) {
        //authenticate { username, pasword }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        //腳色
        List<String> roles = userDetails.getAuthorities().stream()
                                                         .map(item -> item.getAuthority())
                                                         .collect(Collectors.toList());

        log.info("roles:{}",roles);
        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails,roles);
        servletResponse.setHeader(HttpHeaders.SET_COOKIE, jwtToken);

        return MgrResponseDto.success(userInfoResponse);
    }

    @ApiOperation(value = "用户註冊", httpMethod = "POST")
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = User.builder()
                        .username(signUpRequest.getUsername())
                        .email(signUpRequest.getEmail())
                        .password(encoder.encode(signUpRequest.getPassword()))
                        .build();
        //取的註冊腳色
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("用戶成功註冊"));
    }

    @ApiOperation(value = "用户登出", httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logoutUser() {
        //clear the Cookie.
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("已被登出"));
    }


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
