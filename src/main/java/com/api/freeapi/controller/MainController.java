package com.api.freeapi.controller;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.dto.UserDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;



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
    @GetMapping("/searchKeyword/{contexts}/{key}/{page}/{pageSize}")
    public ResponseResult searchContext(@NonNull @PathVariable String contexts, @PathVariable String key,@PathVariable Integer page,@PathVariable Integer pageSize){
        ResponseResult search = mainService.searchKeyWord(contexts,key,page,pageSize);
        return search;
    }

    @GetMapping("/searchPage/{key}/{page}/{pageSize}")
    public ResponseResult searchAllContext(@PathVariable String key,@PathVariable int page,@PathVariable int pageSize){
        return mainService.searchPage(key,page,pageSize);
    }
}
