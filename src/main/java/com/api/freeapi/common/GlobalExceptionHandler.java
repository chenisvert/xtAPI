package com.api.freeapi.common;


import com.api.freeapi.utils.MyUtils;
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

        log.error("请求URL-----------------" + MyUtils.getRequest().getRequestURL());
        log.error("出错啦-----------------" + ex.getMessage());

        log.error("出现异常！"+ex.getMessage());
        if (ex instanceof UserException) {
            UserException e = (UserException) ex;
            return ResponseResult.error(403,e.getMessage());
        }
        //返回默认错误
        return ResponseResult.error(SERVER_ERROR.getErrCode(),SERVER_ERROR.getErrMsg());
    }


}