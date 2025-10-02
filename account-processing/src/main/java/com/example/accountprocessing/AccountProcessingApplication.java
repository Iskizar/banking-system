package com.example.accountprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.example.accountprocessing",
        "com.example.common.logging",
        "com.example.aspects"
})
@EnableJpaRepositories(basePackages = {
        "com.example.accountprocessing.repository",
        "com.example.common.logging.repository"
})
@EntityScan(basePackages = {
        "com.example.accountprocessing.entity",
        "com.example.common.logging.entity"
})
public class AccountProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountProcessingApplication.class, args);
    }
}


