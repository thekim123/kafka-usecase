package com.namusd.jwtredis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseUtil {

    private static volatile ObjectMapper objectMapper; // volatile 선언

    private ParseUtil() {
        // private 생성자 - 외부에서 인스턴스 생성 금지
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (ParseUtil.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper(); // ObjectMapper 초기화
                }
            }
        }
        return objectMapper;
    }

    // Helper 메서드: 객체를 JSON 문자열로 변환
    public static String toJson(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    // Helper 메서드: JSON 문자열을 객체로 변환
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
}
