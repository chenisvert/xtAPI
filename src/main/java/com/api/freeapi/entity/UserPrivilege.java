package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_privilege")
public class UserPrivilege implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer maxSize;

    private Integer apiId;

    private Integer userIdentity;
}
