package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 淘淘商城统一响应结果封装类
 * 用于封装API接口的返回结果，包含状态码、消息和数据
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class TaotaoResult implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaotaoResult.class);

    /**
     * Jackson对象映射器，用于JSON序列化和反序列化
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态码：200表示成功，其他值表示失败
     */
    private Integer status;

    /**
     * 响应消息，描述操作结果
     */
    private String msg;

    /**
     * 响应数据，存放实际返回的业务数据
     */
    private Object data;

    /**
     * 构建响应结果对象
     * 
     * @param status 状态码
     * @param msg 消息描述
     * @param data 响应数据
     * @return TaotaoResult实例
     */
    public static TaotaoResult build(Integer status, String msg, Object data) {
        return new TaotaoResult(status, msg, data);
    }

    /**
     * 构建成功响应结果（带数据）
     * 
     * @param data 响应数据
     * @return TaotaoResult实例，状态码为200，消息为"OK"
     */
    public static TaotaoResult ok(Object data) {
        return new TaotaoResult(data);
    }

    /**
     * 构建成功响应结果（无数据）
     * 
     * @return TaotaoResult实例，状态码为200，消息为"OK"
     */
    public static TaotaoResult ok() {
        return new TaotaoResult(null);
    }

    /**
     * 无参构造函数
     */
    public TaotaoResult() {

    }

    /**
     * 构建响应结果对象（无数据）
     * 
     * @param status 状态码
     * @param msg 消息描述
     * @return TaotaoResult实例
     */
    public static TaotaoResult build(Integer status, String msg) {
        return new TaotaoResult(status, msg, null);
    }

    /**
     * 全参构造函数
     * 
     * @param status 状态码
     * @param msg 消息描述
     * @param data 响应数据
     */
    public TaotaoResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构造成功响应（带数据）
     * 
     * @param data 响应数据
     */
    public TaotaoResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

//    public Boolean isOK() {
//        return this.status == 200;
//    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将JSON字符串反序列化为TaotaoResult对象，并将data字段转换为指定类型
     * 
     * @param jsonData JSON数据字符串，不能为空
     * @param clazz data字段的目标类型，可为null（表示不转换data）
     * @return TaotaoResult实例，若解析失败返回null
     */
    public static TaotaoResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, TaotaoResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化为TaotaoResult对象（不转换data字段类型）
     * 
     * @param json JSON数据字符串，不能为空
     * @return TaotaoResult实例，若解析失败返回null
     */
    public static TaotaoResult format(String json) {
        try {
            return MAPPER.readValue(json, TaotaoResult.class);
        } catch (Exception e) {
            logger.error("JSON转TaotaoResult失败", e);
        }
        return null;
    }

    /**
     * 将JSON字符串反序列化为TaotaoResult对象，并将data字段转换为List集合
     * 
     * @param jsonData JSON数据字符串，不能为空
     * @param clazz List中元素的目标类型
     * @return TaotaoResult实例，data字段为List集合，若解析失败返回null
     */
    public static TaotaoResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
