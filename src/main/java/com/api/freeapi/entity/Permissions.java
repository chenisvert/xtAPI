package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permissions {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer identity;

    private String perms;
}
