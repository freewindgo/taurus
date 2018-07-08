package com.cf.taurus.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JsonUtils
 *
 * @author 于文硕
 * @since 2018/5/25 18:22
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = init();

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static ObjectMapper init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    private static <T> T parseObject(String text, JavaType type) throws JsonUtilsException {
        try {
            return objectMapper.readValue(text, type);
        } catch (Throwable var3) {
            throw new JsonUtilsException(var3.getMessage(), var3);
        }
    }

    public static <T> T parseObject(String text, Class<T> type) throws JsonUtilsException {
        return parseObject(text, objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> List<T> parseList(String text, Class<T> type) {
        return (List)parseObject(text, (JavaType)objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type));
    }

    public static Map<String, Object> parseMap(String text) {
        return parseMap(text, String.class, Object.class);
    }

    public static <K, V> Map<K, V> parseMap(String text, Class<K> k, Class<V> v) {
        return (Map)parseObject(text, (JavaType)objectMapper.getTypeFactory().constructMapType(HashMap.class, k, v));
    }

    public static String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Throwable var2) {
            throw new JsonUtilsException(var2.getMessage(), var2);
        }
    }

    public static void writeJSONString(OutputStream out, Object object) {
        try {
            objectMapper.writeValue(out, object);
        } catch (Throwable var3) {
            throw new JsonUtilsException(var3.getMessage(), var3);
        }
    }

    public static ObjectNode parseJsonObject(String json) {
        try {
            return (ObjectNode)objectMapper.readValue(json, ObjectNode.class);
        } catch (Throwable var2) {
            throw new JsonUtilsException(var2.getMessage(), var2);
        }
    }

    public static ArrayNode parseJsonArray(String json) {
        try {
            return (ArrayNode)objectMapper.readValue(json, ArrayNode.class);
        } catch (Throwable var2) {
            throw new JsonUtilsException(var2.getMessage(), var2);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Throwable var2) {
            throw new JsonUtilsException(var2.getMessage(), var2);
        }
    }

    public static ObjectNode createNode() {
        return objectMapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }

    public static class JsonUtilsException extends RuntimeException {
        public JsonUtilsException() {
        }

        public JsonUtilsException(String message) {
            super(message);
        }

        public JsonUtilsException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
