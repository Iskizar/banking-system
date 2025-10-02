package com.example.accountprocessing.controller;

import com.example.accountprocessing.service.TestHttpService;
import com.example.common.logging.annotation.HttpIncomeRequestLog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestHttpController {

    private final TestHttpService testHttpService;

    @GetMapping("/test-http-log")
    public String testLog() {
        return testHttpService.testHttpRequest();
    }
    @HttpIncomeRequestLog
    @GetMapping("/test-income-log")
    public String testIncomeLog() {
        return "HTTP income request log test";
    }
}
