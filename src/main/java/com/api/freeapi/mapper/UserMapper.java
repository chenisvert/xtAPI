package com.api.freeapi.mapper;

import com.api.freeapi.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set visit_size=visit_size+1  where  id=#{id}")
    Integer addVisitCountById(@Param("id") Integer id);

    @Select("select visit_size from user where id=#{id}")
    Integer selectVisitCountById(@Param("id") Integer id);

    @Select("select username from user where id in (select uid from context where id=#{id})")
    String selectUserNameByContextId(@Param("id") Integer id);

    @Select("select uuid from user where username=#{username}")
    String selectUuidByUserName(@Param("username")String username);

    @Select("select username from user where uuid=#{uuid}")
    String selectUsernameByUUid(@Param("uuid")String uuid);

    @Select("select id from user where username=#{username}")
    Integer queryUidByUsername(@Param("username")String username);


}
