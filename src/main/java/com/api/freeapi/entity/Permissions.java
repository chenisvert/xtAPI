package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Permissions implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer identity;

    private String perms;
}
