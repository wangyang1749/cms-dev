package com.wangyang.model.pojo.dto;

import com.wangyang.model.pojo.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private List<Role> roles;
}
