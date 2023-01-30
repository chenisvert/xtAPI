package com.api.freeapi.entity.vo;

import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserVO extends Context implements Serializable {

    private List<User> user = new ArrayList<>();

    private String uuid;
}
