package com.api.freeapi.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserInfo implements Serializable {

    private String username;

    private Integer thumbsUp; //总点赞量

    private Integer integral; //积分

    private Integer Diamonds; //钻石
}
