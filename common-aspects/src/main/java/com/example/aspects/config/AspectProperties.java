package com.example.aspects.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aspect")
public class AspectProperties {
    private String serviceName;
    private String kafkaTopic;
}
