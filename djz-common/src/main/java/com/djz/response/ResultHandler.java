package com.djz.response;


import cn.hutool.extra.spring.SpringUtil;
import com.djz.handler.MessageSourceHandler;

public class ResultHandler {

    private static final MessageSourceHandler messageSourceHandler = SpringUtil.getBean(MessageSourceHandler.class);

    /**
     * 成功时生成ResultBean对象
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok() {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功时生成ResultBean对象
     * @param data 数据
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok(T data) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功时生成ResultBean对象
     * @param data 数据
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok(T data, String message) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), messageSourceHandler.getMessage(message), data);
//        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, data);
    }


    /**
     * 成功时生成ResultBean对象
     * @param data 数据
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> ok(T data, String message, Object... msgData) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), messageSourceHandler.getMessage(message, msgData), data);
//        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, data);
    }


    /**
     * 成功时生成ResultBean对象
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> okMsg(String message) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), messageSourceHandler.getMessage(message), null);
//        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 成功时生成ResultBean对象
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> okMsg(String message, Object... msgData) {
        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), messageSourceHandler.getMessage(message, msgData), null);
//        return new ResultBean<>(ResultEnum.SUCCESS.getCode(), message, null);
    }


    /**
     * 失败时生成ResultBean对象
     * @return ResultBean
     */
    public static <T> ResultBean<T> error() {
        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), ResultEnum.EXCEPTION.getMessage(), null);
    }

    /**
     * 失败时生成ResultBean对象
     * @param error  错误类型
     * @return  ResultBean
     */
    public static <T> ResultBean<T> error(ResultEnum error) {
        return new ResultBean<>(error.getCode(), error.getMessage(), null);
    }

    /**
     * 失败时生成ResultBean对象
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> error(String message) {
        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), messageSourceHandler.getMessage(message), null);
//        return new ResultBean<>(ResultEnum.EXCEPTION.getCode(), message, null);
    }

    /**
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> resultBean(Integer code, String message) {
        return new ResultBean<>(code, messageSourceHandler.getMessage(message), null);
//        return new ResultBean<>(code, message, null);
    }

    /**
     * @param message 消息
     * @return ResultBean
     */
    public static <T> ResultBean<T> resultBean(Integer code, String message, T data) {
        return new ResultBean<>(code, messageSourceHandler.getMessage(message), data);
//        return new ResultBean<>(code, message, data);
    }
}
