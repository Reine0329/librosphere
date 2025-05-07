package com.sakura.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;         // 账号
    private String password;        // 密码
    private String name;            // 姓名
    private String authority;       // 角色：1代表管理员，2代表用户
    private String phone;           // 号码
}
