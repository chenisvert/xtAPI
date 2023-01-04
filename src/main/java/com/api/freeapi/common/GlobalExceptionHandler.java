package com.api.freeapi.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.api.freeapi.common.ErrorCode.SERVER_ERROR;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class}) //配置拦截
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {



    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public ResponseResult exceptionHandler(Throwable ex){
        log.error("出现异常！"+ex.getMessage());
        return ResponseResult.error(SERVER_ERROR.getErrCode(),SERVER_ERROR.getErrMsg());
    }


    /**
     * 业务异常处理方法
     * @return
     */
    @ExceptionHandler(UserException.class)
    public ResponseResult exceptionHandler(UserException ex){
        log.error(ex.getMessage());
        return ResponseResult.error(403,ex.getMessage());
    }
}