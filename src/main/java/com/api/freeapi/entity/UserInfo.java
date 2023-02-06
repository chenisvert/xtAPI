package com.api.freeapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo implements Serializable {

    private String username;

    private Integer thumbsUp; //总点赞量

    private Integer integral; //积分

    private Integer Diamonds; //钻石

    private String url ;
}
