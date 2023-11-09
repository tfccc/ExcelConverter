package com.example.excelconverter.entity;

import java.util.HashMap;
import java.util.Objects;


public class ComResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 初始化一个新创建的 Message 对象
     */
    public ComResult() {
    }

    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static ComResult error() {
        return error(1, "操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 内容
     * @return 错误消息
     */
    public static ComResult error(String msg) {
        return error(500, msg);
    }

    /**
     * 返回错误消息
     *
     * @param msg 内容
     * @return 错误消息
     */
    public static ComResult error(String msg, Object data) {
        return error(500, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 响应码
     * @param msg  内容
     * @param data 响应错误时返回的数据
     * @return 错误消息
     */
    public static ComResult error(int code, String msg, Object data) {
        ComResult json = new ComResult();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json;
    }

    /**
     * 返回错误消息
     *
     * @param code 错误码
     * @param msg  内容
     * @return 错误消息
     */
    public static ComResult error(int code, String msg) {
        ComResult json = new ComResult();
        json.put("code", code);
        json.put("msg", msg);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @param msg 内容
     * @return 成功消息
     */
    public static ComResult success(String msg) {
        ComResult json = new ComResult();
        json.put("msg", msg);
        json.put("code", 200);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @param msg  成功消息
     * @param data 数据
     * @return
     */
    public static ComResult success(String msg, Object data) {
        ComResult json = new ComResult();
        json.put("msg", Objects.isNull(msg) ? "操作成功" : msg);
        json.put("data", data);
        json.put("code", 200);
        return json;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ComResult success() {
        return ComResult.success("操作成功");
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ComResult success(Object data) {
        return ComResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param key   键值
     * @param value 内容
     * @return 成功消息
     */
    @Override
    public ComResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
