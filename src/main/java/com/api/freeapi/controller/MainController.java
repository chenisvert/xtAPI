package com.api.freeapi.controller;

import com.api.freeapi.anno.AccessLimit;
import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.entity.vo.UserVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


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
    @AccessLimit(sec = 5, limit = 1)
    @ResponseBody
    @PostMapping("/insert")
    public ResponseResult insertContext(@RequestBody UserVO contextDto){
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
    @AccessLimit(sec = 1, limit = 1)
    @GetMapping("/searchKeyword/{contexts}/{key}/{page}/{pageSize}")
    public ResponseResult searchContext(@NonNull @PathVariable String contexts, @PathVariable String key,@PathVariable Integer page,@PathVariable Integer pageSize){
        ResponseResult search = mainService.searchKeyWord(contexts,key,page,pageSize);
        return search;
    }

    /***
     * 查询留言（分页）
     * @Author chen
     * @Date  14:17
     * @Param  page pageSize
     * @Return ResponseResult
     * @Since version-11

     */
    @AccessLimit(sec = 1, limit = 5)
    @GetMapping("/searchPage/{key}/{page}/{pageSize}")
    public ResponseResult searchAllContext(@PathVariable String key,@PathVariable int page,@PathVariable int pageSize){
        return mainService.searchPage(key,page,pageSize);
    }

    /***
     *
     * 点赞
     * @Author chen
     * @Date  14:18
     * @Param id
     * @Return ResponseResult
     * @Since version-11

     */

    @AccessLimit(sec = 1, limit = 5)
    @GetMapping("/ThumbsUp/{id}")
    public ResponseResult giveThumbsUp(@PathVariable Integer id){
        return mainService.giveThumbsUp(id);
    }
}
