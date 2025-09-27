package com.example.creditprocessing.controller;

import com.example.creditprocessing.dto.CreditRequestDto;
import com.example.creditprocessing.service.CreditProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditProcessingController {

    private final CreditProcessingService creditProcessingService;

    @PostMapping("/test")
    public ResponseEntity<String> testCredit(@RequestBody CreditRequestDto request) {
        try {
            creditProcessingService.processCredit(
                    request.getClientId(),
                    request.getProductId(),
                    request.getSum(),
                    request.getMonthCount()
            );
            return ResponseEntity.ok("Заявка успешно обработана");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
}
