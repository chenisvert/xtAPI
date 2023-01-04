package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private int id;

    private String username;

    private String password;

    private String uuid;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
