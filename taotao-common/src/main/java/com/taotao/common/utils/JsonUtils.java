package com.taotao.common.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON工具类
 * 提供对象与JSON字符串之间的序列化和反序列化功能
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Jackson对象映射器，用于JSON序列化和反序列化
     * 使用静态常量保证全局唯一实例，避免重复创建开销
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象序列化为JSON字符串
     * 
     * @param data 待序列化的对象，可为null
     * @return JSON字符串，若序列化失败返回null
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            logger.error("JSON序列化失败", e);
        }
        return null;
    }

    /**
     * 将JSON字符串反序列化为指定类型的对象
     * 
     * @param jsonData JSON数据字符串，不能为空
     * @param beanType 目标对象的类型Class
     * @return 反序列化后的对象，若失败返回null
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            logger.error("JSON转POJO失败", e);
        }
        return null;
    }

    /**
     * 将JSON字符串反序列化为指定类型的List集合
     * 
     * @param jsonData JSON数据字符串，不能为空
     * @param beanType List中元素的类型Class
     * @return 反序列化后的List集合，若失败返回null
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            logger.error("JSON转List失败", e);
        }

        return null;
    }

}
