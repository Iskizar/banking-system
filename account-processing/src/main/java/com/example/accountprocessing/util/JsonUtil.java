
package com.example.accountprocessing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private static ObjectMapper mapper;

    public JsonUtil(ObjectMapper mapper) {
        this.mapper = mapper; // Spring уже даёт ObjectMapper с нужными настройками
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
