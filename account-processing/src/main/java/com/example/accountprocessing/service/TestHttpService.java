package com.example.accountprocessing.service;

import com.example.common.logging.annotation.HttpOutcomeRequestLog;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestHttpService {

    private final RestTemplate restTemplate = new RestTemplate();

    @HttpOutcomeRequestLog
    public String testHttpRequest() {
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos/1", String.class);
    }
}
