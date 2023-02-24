package com.api.freeapi.service.Impl;

import com.api.freeapi.common.ResponseResult;
import com.api.freeapi.common.UserException;
import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import com.api.freeapi.entity.UserInfo;
import com.api.freeapi.entity.vo.UserVO;
import com.api.freeapi.mapper.MainMapper;
import com.api.freeapi.mapper.UserInfoMapper;
import com.api.freeapi.mapper.UserMapper;
import com.api.freeapi.service.MainService;
import com.api.freeapi.service.UserService;
import com.api.freeapi.utils.IPUtil;
import com.api.freeapi.utils.IpAddressUtils;
import com.api.freeapi.utils.RedisUtil;
import com.api.freeapi.utils.Xlsx;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.api.freeapi.common.ErrorCode.*;
import static com.api.freeapi.common.RedisKey.KEY_SEARCH;

@Slf4j
@Service
public class MainServiceImpl  extends ServiceImpl<MainMapper, Context> implements MainService {

    @Value("${file.basepath.filepath}")
    private String filepath;

    @Resource
    private MainMapper mainMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserService userService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisTemplate redisTemplate;

    private HashMap<Object, Object> map = new HashMap<>();

    @Override
    public Boolean checkKeyUrl(String username) {

        String origin = request.getHeader("Origin");

        List<UserInfo> url = userInfoMapper.selectUrlByUsername(username);
        String urls = null;
        for (UserInfo userInfo : url) {
            urls = userInfo.getUrl();
        }
        if (StringUtils.isBlank(urls)) {
            throw new UserException(KEY_ERROR.getErrMsg());
        }
        log.info("origin:{}", origin);
        log.info("urls:{}", urls);
        //截取
        String[] split = urls.split(",");
        for (String s : split) {
            if (!StringUtils.isBlank(origin) & s.equals(origin)) {
                log.info("授权域名成功：{}", s);
                return true;
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult insert(UserVO userVO) {
        log.info("insert方法 入参：{}", userVO);
        //判空
        if (StringUtils.isBlank(userVO.getName()) | StringUtils.isBlank(userVO.getEmail()) | StringUtils.isBlank(userVO.getContext())) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }

        String uuid = userVO.getUuid();
        String username = userMapper.selectUsernameByUUid(uuid);
        Boolean check = this.checkKeyUrl(username);
        log.info("insert 授权:{}", check);
        if (!check) {
            throw new UserException("授权失败");
        }
        Context context = new Context();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        List<User> user = userMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(user)) {
            throw new UserException(KEY_ERROR.getErrMsg());
        }
        Integer uid = 0;
        //判断是否有uuid
        for (User user1 : user) {
            uid = user1.getId();
        }
        String ipAddr = IPUtil.getIpAddr(request);
        String address = IpAddressUtils.getIpSource(ipAddr);
        context.setUid(uid);
        context.setIp(ipAddr);
        context.setAddress(address);
        context.setName(userVO.getName());
        context.setEmail(userVO.getEmail());
        context.setContext(userVO.getContext());
        context.setAvatar(userVO.getAvatar());
        save(context);
        //内容缓存
        Set keysPage = redisTemplate.keys(KEY_SEARCH + "_" + "searchPage" + "_" + uuid + "_" + 1 + "_" + "*");
        //关键词查询缓存
        Set keysKeyWord = redisTemplate.keys(KEY_SEARCH + "_" + "KeyWord" + "_" + uuid + "_" + "_" + 1 + "_" + "*");
        //删除缓存
        redisTemplate.delete(keysPage);
        redisTemplate.delete(keysKeyWord);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectPage(int page, int pageSize) {
        log.info("selectPage方法 入参 page{} , pageSize:{}", page, pageSize);
        String username = userService.getTokenInfo();
        String key = userService.selectUserKeyByUserName(username);
        List<User> userList = userService.verifyKey(key);
        if (CollectionUtils.isEmpty(userList)) {
            return ResponseResult.error(403, "服务异常");
        }
        Integer id = null;
        for (User user : userList) {
            id = user.getId();
        }
        log.info("selectPage方法 key：{}", key);
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Context::getUid, id)
                .orderByDesc(Context::getId)
                .select(Context::getId, Context::getUid, Context::getName, Context::getEmail, Context::getContext, Context::getAvatar, Context::getAddress, Context::getCreateTime, Context::getIp);


        Page pageInfo = new Page(page, pageSize);
        Page page1 = mainMapper.selectPage(pageInfo, queryWrapper);
        log.info("selectPage 方法 page:{}", page1);
        map.put("list", page1);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult deleteById(Integer id) {
        if (id == null) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        Integer count = mainMapper.deleteById(id);
        if (count > 1) {
            ResponseResult.error(403, "留言不存在");
        }
        //内容缓存
        Set keysPage = redisTemplate.keys(KEY_SEARCH + "_" + "searchPage" + "_*");
        //关键词查询缓存
        Set keysKeyWord = redisTemplate.keys(KEY_SEARCH + "_" + "KeyWord" + "_*");
        //删除缓存
        redisTemplate.delete(keysPage);
        redisTemplate.delete(keysKeyWord);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult searchKeyWord(String contexts, String key, Integer page, Integer pageSize) {

        if (StringUtils.isBlank(key)) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        String username = userMapper.selectUsernameByUUid(key);
        Boolean check = this.checkKeyUrl(username);
        if (!check) {
            throw new UserException("授权失败");
        }
        Object contextPageCache = RedisUtil.get(KEY_SEARCH + "_" + "KeyWord" + "_" + key + "_" + "_" + page + "_" + pageSize + "_" + contexts);
        if (contextPageCache != null) {
            map.put("list", contextPageCache);
            return ResponseResult.success(map);
        }
        LambdaQueryWrapper<User> UserQueryWrapper = new LambdaQueryWrapper<>();
        UserQueryWrapper.eq(User::getUuid, key);
        UserQueryWrapper.select(User::getId);
        log.info("key是：{}", key);
        List<User> usersList = userMapper.selectList(UserQueryWrapper);
        //判断是否有key
        if (CollectionUtils.isEmpty(usersList)) {
            throw new UserException(KEY_ERROR.getErrMsg());
        }
        User user = new User();
        usersList.stream().map((item) -> {
            user.setId(item.getId());
            return user;
        }).collect(Collectors.toList());
        log.info("id {}", user.getId());
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid, user.getId());
        queryWrapper.like(Context::getContext, contexts);
        //按照时间倒叙
        queryWrapper.orderByDesc(Context::getId, Context::getThumbsUp);
        queryWrapper.select(Context::getEmail, Context::getContext, Context::getCreateTime, Context::getName, Context::getAddress, Context::getAvatar);
        Page<Context> pageInfo = new Page<>(page, pageSize);

        Page pageContext = mainMapper.selectPage(pageInfo, queryWrapper);
        RedisUtil.set(KEY_SEARCH + "_" + "KeyWord" + "_" + key + "_" + "_" + page + "_" + pageSize + "_" + contexts, pageContext, 60);

        map.put("list", pageContext);
        log.info("模糊查询 {}", pageContext);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult searchPage(String key, int page, int pageSize) {
        if (StringUtils.isBlank(key)) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }
        String username = userMapper.selectUsernameByUUid(key);
        Boolean check = this.checkKeyUrl(username);
        if (!check) {
            throw new UserException("授权失败");
        }

        Object pageCache = RedisUtil.get(KEY_SEARCH + "_" + "searchPage" + "_" + key + "_" + page + "_" + pageSize);
        if (!Objects.isNull(pageCache)) {
            map.put("list", pageCache);
            return ResponseResult.success(map);
        }
        Page pageInfo = new Page(page, pageSize);
        log.info("key是：{}", key);
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUuid, key);
        userQueryWrapper.select(User::getId);
        List<User> usersList = userMapper.selectList(userQueryWrapper);
        //判断是否有key
        if (CollectionUtils.isEmpty(usersList)) {
            throw new UserException(KEY_ERROR.getErrMsg());
        }

        User user = new User();
        usersList.stream().map((item) -> {
            user.setId(item.getId());
            return user;
        }).collect(Collectors.toList());
        log.info("id {}", user.getId());
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid, user.getId());
        //按照id和点赞量倒叙
        queryWrapper.orderByDesc(Context::getId, Context::getThumbsUp);
        queryWrapper.select(Context::getName, Context::getThumbsUp, Context::getEmail, Context::getContext, Context::getCreateTime, Context::getAddress, Context::getId, Context::getAvatar);
        //查询
        Page page1 = mainMapper.selectPage(pageInfo, queryWrapper);
        List<Context> list = mainMapper.selectMaxThumbsUpById(user.getId());
        if (CollectionUtils.isEmpty(list)) {
            throw new UserException("暂无留言");
        }
        Integer maxThumbs = null;
        for (Context context : list) {
            maxThumbs = context.getThumbsUp();
        }
        maxThumbs = maxThumbs - 10;
        RedisUtil.set(KEY_SEARCH + "_" + "searchPage" + "_" + key + "_" + page + "_" + pageSize, page1, 300);
        map.put("list", page1);
        map.put("hot", maxThumbs);
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult giveThumbsUp(Integer id) {
        if (id == null) {
            throw new UserException(PARAMS_ERROR.getErrMsg());
        }

        String username = userMapper.selectUserNameByContextId(id);
        if (StringUtils.isBlank(username)) {
            throw new UserException(USERNAME_ERROR.getErrMsg());
        }
        mainMapper.addGiveThumbsUpById(id);
        userInfoMapper.addThumbsCountByUsername(username);
        String uuid = userMapper.selectUuidByUserName(username);

        List<Context> contextList = mainMapper.selectThumbsUpById(id);
        Set keysPage = redisTemplate.keys(KEY_SEARCH + "_" + "searchPage" + "_" + uuid + "_*");
        redisTemplate.delete(keysPage);

        Integer thumbs = null;
        for (Context context : contextList) {
            thumbs = context.getThumbsUp();
        }
        map.put("thumbs", thumbs);
        return ResponseResult.success(map);
    }

    public ResponseResult writeDbtoExcel(Integer uid) throws IOException {

        List<String> titleList = new ArrayList<String>();
        titleList.add("uid");
        titleList.add("name");
        titleList.add("email");
        titleList.add("context");
        titleList.add("createTime");
        titleList.add("address");
        titleList.add("ip");
        titleList.add("thumbsUp");
        titleList.add("avatar");
        List<String> content = null;
        List<List<String>> contentsList = new ArrayList<List<String>>();
        LambdaQueryWrapper<Context> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Context::getUid, uid).orderByDesc(Context::getId);
        List<Context> contextList = mainMapper.selectList(queryWrapper);
//        contextList.stream().map((item) -> {
//            content.add(item.getUid());
//            content.add(item.getName());
//            content.add(item.getEmail());
//            content.add(item.getContext());
//            content.add(item.getCreateTime());
//            content.add(item.getAddress());
//            content.add(item.getIp());
//            content.add(item.getThumbsUp());
//            content.add(item.getAvatar());
//            return content;
//        }).collect(Collectors.toList());
        for (Context item:contextList) {
            content = new ArrayList<String>();
            content.add(String.valueOf(item.getUid()));
            content.add(item.getName());
            content.add(item.getEmail());
            content.add(item.getContext());
            content.add(String.valueOf(item.getCreateTime()));
            content.add(item.getAddress());
            content.add(item.getIp());
            content.add(String.valueOf(item.getThumbsUp()));
            content.add(item.getAvatar());
            contentsList.add(content);
        }


        XSSFWorkbook workBook = null;
        FileOutputStream output = null;
        String sheetName = System.currentTimeMillis()+"_context_"+uid;
        String filePaths =  filepath+"/backup/"+sheetName+".xlsx";
        File uploadFile=new File(filepath+"/backup/");
        //判断目录是否存在
        if(!uploadFile.exists()){
            uploadFile.mkdir();
        }
        log.info("保存位置：{}",filePaths);
        String fileName = filePaths;

            try {
                Xlsx eu = new Xlsx();
                workBook = eu.getWorkBook(titleList, contentsList, sheetName);
                output = new FileOutputStream(fileName);
                workBook.write(output);
            } catch (FileNotFoundException e) {
                log.error("文件不存在 {}",filePaths);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            }
            map.put("url","http://api.hh2022.cn/user/downloadFile/"+sheetName+".xlsx");
            return ResponseResult.success(map);
    }

}