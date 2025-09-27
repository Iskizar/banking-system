package com.example.clientprocessing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.app.client")
public class ClientProperties {
    private String regionCode;
    private String departmentCode;

}
