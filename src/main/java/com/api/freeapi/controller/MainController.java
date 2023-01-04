package com.api.freeapi.controller;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static com.api.freeapi.common.RedisKey.Search_Key;

@Slf4j
@RestController
@RequestMapping("/main")
public class MainController extends BaseController {

    /***
     *
     * 插入留言
     * @Author chen
     * @Date  17:08
     * @Param
     * @Return
     * @Since version-11

     */
    @ResponseBody
    @PostMapping("/insert")
    public ResponseResult insertContext(@RequestBody UserDto contextDto){
        log.info("入参",contextDto);
        System.out.println("========================"+contextDto);
        return mainService.insert(contextDto);
    }

    /***
     *
     * 根据标题查询留言
     * @Author chen
     * @Date  17:08
     * @Param
     * @Return
     * @Since version-11

     */
    @GetMapping("/search/{contexts}/{key}")
    public ResponseResult searchContext(@PathVariable String contexts,@PathVariable String key){
        ResponseResult context = (ResponseResult) redisTemplate.opsForValue().get(Search_Key + contexts+key);
        if (context != null){
            return context;
        }
        ResponseResult search = mainService.search(contexts,key);
        redisTemplate.opsForValue().set(Search_Key+contexts+key,search, 1,TimeUnit.MINUTES);
        return search;
    }

    @GetMapping("/searchAll/{key}/{page}/{pageSize}")
    public ResponseResult searchAllContext(@PathVariable String key,@PathVariable int page,@PathVariable int pageSize){
        return mainService.searchAll(key,page,pageSize);
    }
}
