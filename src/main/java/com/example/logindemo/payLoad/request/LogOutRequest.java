package com.example.logindemo.payLoad.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "登出請求")
@Getter
public class LogOutRequest {

    @ApiModelProperty(value = "用戶id")
    @NotBlank
    private Long userId;
}
