package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("context")
public class Context implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer uid;

    private String email;

    private String context;

    private String name;

    private String address;

    private String ip;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
