package com.api.freeapi.common;
/**
 * 自定义业务异常类
 */
public class UserException extends RuntimeException {
    public UserException(String message){
        super(message);
    }
}