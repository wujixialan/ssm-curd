package com.zxg.ssmcurd.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxg on 2017/6/10.
 * 返回 json 数据
 */
public class Message {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 用户要返回给浏览器的数据
     */
    private Map<String, Object> map = new HashMap<>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public static Message success() {
        Message message = new Message();
        message.setCode(200);
        message.setMsg("处理成功");
        return message;
    }

    public static Message fail() {
        Message message = new Message();
        message.setCode(400);
        message.setMsg("处理失败");
        return message;
    }

    public Message add(String key, Object value) {
        this.getMap().put(key, value);
        return this;
    }
}
