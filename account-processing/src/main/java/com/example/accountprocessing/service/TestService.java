package com.example.accountprocessing.service;

import com.example.common.logging.annotation.LogDatasourceError;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @LogDatasourceError
    public void testError() {
        // Искусственно кидаем исключение
        throw new RuntimeException("Test error from SomeService");
    }
}
