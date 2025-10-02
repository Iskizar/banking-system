package com.example.aspects;

import com.example.aspects.config.AspectProperties;
import com.example.common.logging.entity.ErrorLog;
import com.example.common.logging.repository.ErrorLogRepository;
import com.example.common.logging.dto.ErrorEvent;
import com.example.common.logging.annotation.LogDatasourceError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DatasourceErrorAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper mapper;
    private final AspectProperties aspectProperties;

    @Around("@annotation(logDatasourceError)")
    public Object around(ProceedingJoinPoint pjp, LogDatasourceError logDatasourceError) throws Throwable {
        log.info("DatasourceErrorAspect triggered for method: {}", pjp.getSignature());
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            log.error("Exception caught in aspect: {}", ex.getMessage(), ex);

            ErrorEvent event = new ErrorEvent(
                    LocalDateTime.now(),
                    pjp.getSignature().toString(),
                    stacktraceToString(ex),
                    ex.getMessage(),
                    pjp.getArgs() != null ? Arrays.asList(pjp.getArgs()) : null
            );

            try {
                String payload = mapper.writeValueAsString(event);

                var message = MessageBuilder.withPayload(payload)
                        .setHeader(KafkaHeaders.TOPIC, "service_logs")
                        .setHeader(KafkaHeaders.KEY, aspectProperties.getServiceName())
                        .setHeader("type", "ERROR")
                        .setHeader("value", "ERROR")
                        .build();

                log.info("Sending error to Kafka topic {}: {}", "service_logs", payload);
                kafkaTemplate.send(message).get(); // синхронная отправка
                log.info("Successfully sent error to Kafka topic {}", "service_logs");

            } catch (Exception kafkaEx) {
                log.error("Failed to send error to Kafka — fallback to DB", kafkaEx);

                ErrorLog logEntry = new ErrorLog();
                logEntry.setServiceName(aspectProperties.getServiceName());
                logEntry.setLogType("ERROR");
                logEntry.setPayload(mapper.writeValueAsString(event));
                logEntry.setCreatedAt(LocalDateTime.now());

                errorLogRepository.save(logEntry);
            }

            throw ex; // пробрасываем исключение дальше
        }
    }

    private String stacktraceToString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement st : ex.getStackTrace()) {
            sb.append(st.toString()).append("\n");
        }
        return sb.toString();
    }
}
