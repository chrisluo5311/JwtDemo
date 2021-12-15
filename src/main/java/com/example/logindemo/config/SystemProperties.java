package com.example.logindemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "system")
@Component
public class SystemProperties {

    private String name;    // system.name
    private String port;    // system.port
    private String logintype;    // system.logintype

}
