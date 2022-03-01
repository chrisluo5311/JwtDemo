package com.example.logindemo.controllers.SessionTestController;

import com.example.logindemo.common.constant.InnerRouteConsts;
import com.example.logindemo.common.response.MgrResponseDto;
import com.example.logindemo.common.session.SessionEntity;
import com.example.logindemo.controllers.core.BaseController;
import com.example.logindemo.exception.responsecode.MgrResponseCode;
import com.example.logindemo.exception.user.UserException;
import com.example.logindemo.models.User;
import com.example.logindemo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@Api(tags = "內部測試Api")
@Slf4j
@RestController
@RequestMapping(InnerRouteConsts.PATH)
public class SessionTestController extends BaseController {

    @Value("${knife4j.production:false}")
    private boolean isProd;

    @Resource
    UserRepository userRepository;

    @Resource
    ObjectMapper objectMapper;

    @ApiOperation(value = "產生sessionEntity", httpMethod = "GET")
    @GetMapping(value = "/session")
    public String createSession(@RequestParam(required = true,defaultValue = "chris") String userName) throws Exception {
        if(isProd) {
            return "無操作權限";
        }
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserException(MgrResponseCode.USER_NOT_FOUND,new Object[]{userName}));
        SessionEntity sessionEntity = SessionEntity.builder().userId(user.getId()).userName(userName).build();
        String writeValueAsString = objectMapper.writeValueAsString(sessionEntity);
        byte[] encode = Base64.getEncoder().encode(writeValueAsString.getBytes());
        return new String(encode);
    }

    @ApiOperation(value = "獲取HttpSession",httpMethod = "GET")
    @GetMapping(value = "/getHttpSession")
    public MgrResponseDto getHttpSessionInfo(){
        log.info("httpSession id:{} AttributeNames:{} isNew:{}",
                getHttpSession().getId(),getHttpSession().getAttributeNames(),getHttpSession().isNew());
        return MgrResponseDto.success();
    }

}
