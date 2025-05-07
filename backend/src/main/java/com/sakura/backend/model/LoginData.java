package com.sakura.backend.model;

import lombok.Data;

@Data
public class LoginData {
    private String role;    //角色
    private String token;   //token
    private String name;    //姓名
}