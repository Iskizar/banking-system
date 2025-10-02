package com.example.aspects;

import com.example.aspects.config.AspectProperties;
import com.example.common.logging.annotation.HttpOutcomeRequestLog;
import com.example.common.logging.dto.HttpRequestLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpOutcomeRequestLogAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final AspectProperties aspectProperties;


    @AfterReturning(pointcut = "@annotation(httpOutcomeRequestLog)", returning = "response")
    public void logHttpRequest(JoinPoint joinPoint, HttpOutcomeRequestLog httpOutcomeRequestLog, Object response) {
        try {
            // Параметры метода
            Map<String, Object> params = new HashMap<>();
            String[] paramNames = joinPoint.getSignature().getDeclaringTypeName() != null
                    ? joinPoint.getArgs().toString().split(",") : new String[]{};

            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params.put("arg" + i, joinPoint.getArgs()[i]);
            }

            String uri = "unknown";

            String paramsAsString = new ObjectMapper().writeValueAsString(params);
            List<Object> argsList = new ArrayList<>();
            argsList.add(response);

            HttpRequestLogEvent event = new HttpRequestLogEvent(
                    LocalDateTime.now(),
                    joinPoint.getSignature().toShortString(),
                    uri,
                    paramsAsString,
                    argsList
            );


            String payload = mapper.writeValueAsString(event);

            var message = MessageBuilder.withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, aspectProperties.getKafkaTopic())
                    .setHeader(KafkaHeaders.KEY, aspectProperties.getServiceName())
                    .setHeader("type", "INFO")
                    .build();

            kafkaTemplate.send(message).get();
            log.info("HTTP request log sent to kafka topic {}", aspectProperties.getKafkaTopic());
        } catch (Exception ex) {
            log.error("Failed to send HTTP request log to Kafka", ex);
        }
    }
}
