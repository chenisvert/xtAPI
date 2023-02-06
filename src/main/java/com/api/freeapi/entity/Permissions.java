package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Permissions implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer identity;

    private String perms;
}
