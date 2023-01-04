package com.api.freeapi.job;

import com.api.freeapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/***
 *
 * 定时任务配置类
 * @Author chen
 * @Date  13:18
 * @Param
 * @Return
 * @Since version-11
 */
@Slf4j
@Configuration    // 1. 代表当前类是一个配置类
@EnableScheduling // 2.开启定时任务
public class SystemJob {
    @Resource
    private UserService userService;
    /***
     *
     * 每天1点重置用户签到状态访问量
     * @Author chen
     * @Date  13:18
     * @Param
     * @Return
     * @Since version-11

     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanAccess() {
        Boolean b = userService.resetSignIn();
        if (b == true){
            log.info("【系统任务】-重置签到成功");
        }else {
            log.error("【系统任务】-无用户需要重置签到");
        }
    }

}
