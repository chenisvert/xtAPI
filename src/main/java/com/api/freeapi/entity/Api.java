package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("api")
public class Api implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private String name;

    private Integer size;

}