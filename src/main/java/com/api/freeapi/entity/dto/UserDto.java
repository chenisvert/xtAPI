package com.api.freeapi.entity.dto;

import com.api.freeapi.entity.Context;
import com.api.freeapi.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto extends Context implements Serializable {

    private List<User> user = new ArrayList<>();

    private Integer uuid;
}
