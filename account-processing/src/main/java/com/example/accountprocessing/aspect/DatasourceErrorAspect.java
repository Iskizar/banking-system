//package com.example.accountprocessing.aspect;
//
//import com.example.common.logging.entity.ErrorLog;
//import com.example.common.logging.repository.ErrorLogRepository;
//import com.example.common.logging.dto.ErrorEvent;
//import com.example.common.logging.annotation.LogDatasourceError;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.support.MessageBuilder;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DatasourceErrorAspect {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ErrorLogRepository errorLogRepository;
//    private final ObjectMapper mapper;
//
//    private final String TOPIC = "service_logs";
//    private final String SERVICE_NAME = "account-processing";
//
//    @Around("@annotation(logDatasourceError)")
//    public Object around(ProceedingJoinPoint pjp, LogDatasourceError logDatasourceError) throws Throwable {
//        try {
//            return pjp.proceed();
//        } catch (Throwable ex) {
//            ErrorEvent event = new ErrorEvent(
//                    LocalDateTime.now(),
//                    pjp.getSignature().toString(),
//                    stacktraceToString(ex),
//                    ex.getMessage(),
//                    pjp.getArgs() != null ? Arrays.asList(pjp.getArgs()) : null
//            );
//
//            String payload = mapper.writeValueAsString(event);
//
//            var message = MessageBuilder.withPayload(payload)
//                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
//                    .setHeader(KafkaHeaders.KEY, SERVICE_NAME)
//                    .setHeader("type", "ERROR")
//                    .setHeader("value", "ERROR")
//                    .build();
//
//
//            try {
//                kafkaTemplate.send(message).get();
//                log.info("Error sent to kafka topic {}", TOPIC);
//            } catch (Exception kafkaEx) {
//                log.error("Failed to send error to Kafka — fallback to DB", kafkaEx);
//                ErrorLog logEntry = new ErrorLog();
//                logEntry.setServiceName(SERVICE_NAME);
//                logEntry.setLogType("ERROR");
//                logEntry.setPayload(payload);
//                logEntry.setCreatedAt(LocalDateTime.now());
//                errorLogRepository.save(logEntry);
//            }
//
//            log.error("Exception in method {}: {}", pjp.getSignature(), ex.getMessage(), ex);
//
//            throw ex;
//        }
//    }
//
//    private String stacktraceToString(Throwable ex) {
//        StringBuilder sb = new StringBuilder();
//        for (StackTraceElement st : ex.getStackTrace()) {
//            sb.append(st.toString()).append("\n");
//        }
//        return sb.toString();
//    }
//}
