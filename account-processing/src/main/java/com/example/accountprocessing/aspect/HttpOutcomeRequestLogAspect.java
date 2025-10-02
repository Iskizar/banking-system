//package com.example.accountprocessing.aspect;
//
//import com.example.common.logging.annotation.HttpOutcomeRequestLog;
//import com.example.common.logging.dto.HttpRequestLogEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.header.internals.RecordHeader;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.support.MessageBuilder;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class HttpOutcomeRequestLogAspect {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper mapper;
//
//    private final String TOPIC = "service_logs";
//    private final String SERVICE_NAME = "account-processing";
//
//    @AfterReturning(pointcut = "@annotation(httpOutcomeRequestLog)", returning = "response")
//    public void logHttpRequest(JoinPoint joinPoint, HttpOutcomeRequestLog httpOutcomeRequestLog, Object response) {
//        try {
//            // Параметры метода
//            Map<String, Object> params = new HashMap<>();
//            String[] paramNames = joinPoint.getSignature().getDeclaringTypeName() != null
//                    ? joinPoint.getArgs().toString().split(",") : new String[]{};
//
//            for (int i = 0; i < joinPoint.getArgs().length; i++) {
//                params.put("arg" + i, joinPoint.getArgs()[i]);
//            }
//
//            String uri = "unknown";
//
//            HttpRequestLogEvent event = new HttpRequestLogEvent(
//                    LocalDateTime.now(),
//                    joinPoint.getSignature().toShortString(),
//                    uri,
//                    params,
//                    response
//            );
//
//            String payload = mapper.writeValueAsString(event);
//
//            var message = MessageBuilder.withPayload(payload)
//                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
//                    .setHeader(KafkaHeaders.KEY, SERVICE_NAME)
//                    .setHeader("type", "INFO")
//                    .build();
//
//            kafkaTemplate.send(message).get();
//            log.info("HTTP request log sent to kafka topic {}", TOPIC);
//        } catch (Exception ex) {
//            log.error("Failed to send HTTP request log to Kafka", ex);
//        }
//    }
//}
