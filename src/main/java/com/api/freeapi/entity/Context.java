package com.api.freeapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("context")
public class Context implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private Integer uid;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "内容不能为空")
    private String context;

    @NotBlank(message = "名字不能为空")
    private String name;

    private String address;

    private String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
