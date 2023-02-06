package com.api.freeapi.anno;

import java.lang.annotation.*;

/***
 *
 * 接口限流
 * @Author chen
 * @Date  18:12
 * @Param
 * @Return
 * @Since version-11

 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {
    //标识限制次数
    int limit() default 5;
    //标识时间
    int sec() default 5;
}