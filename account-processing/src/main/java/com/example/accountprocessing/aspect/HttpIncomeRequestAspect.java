//package com.example.accountprocessing.aspect;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.header.internals.RecordHeader;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class HttpIncomeRequestAspect {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper mapper;
//    private final HttpServletRequest request;
//
//    private final String TOPIC = "service_logs";
//    private final String SERVICE_NAME = "account-processing";
//
//    @Before("@annotation(com.example.common.logging.annotation.HttpIncomeRequestLog)")
//    public void beforeHttpRequest(JoinPoint joinPoint) {
//        try {
//            Map<String, Object> logData = new HashMap<>();
//            logData.put("timestamp", LocalDateTime.now().toString());
//            logData.put("methodSignature", joinPoint.getSignature().toString());
//            logData.put("uri", request.getRequestURI());
//            logData.put("parameters", joinPoint.getArgs());
//
//            String payload = mapper.writeValueAsString(logData);
//
//            var message = MessageBuilder.withPayload(payload)
//                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
//                    .setHeader(KafkaHeaders.KEY, SERVICE_NAME)
//                    .setHeader("type", "INFO")
//                    .setHeader("value", "INFO")
//                    .build();
//
//            kafkaTemplate.send(message).get();
//
//            log.info("HTTP income request log sent to Kafka topic {}", TOPIC);
//
//        } catch (Exception e) {
//            log.error("Failed to send HTTP income request log to Kafka", e);
//        }
//    }
//}
