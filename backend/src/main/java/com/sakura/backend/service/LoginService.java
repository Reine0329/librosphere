package com.sakura.backend.service;

import com.sakura.backend.model.LoginData;
import com.sakura.backend.model.LoginParam;
import com.sakura.backend.model.User;
import com.sakura.backend.repository.LoginRepository;
import com.sakura.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    LoginRepository loginRepository;
    public LoginData login(LoginParam json) {
        String account = json.getAccount();
        String password = json.getPassword();
        String authority = json.getAuthority();
        LoginData data = new LoginData();
        User user = loginRepository.findMemberByAccountAndPasswordAndAuthority(account, password, authority);
        if (user == null) {
            throw new RuntimeException("用户名、密码或身份错误！");
        }
        data.setName(user.getName());
        data.setRole(authority);
        //用jwt工具封装好token返回给前端
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", json.getAuthority());
        claims.put("name", data.getName());
        String token = JwtUtil.genJWT(claims);
        data.setRole(authority);
        data.setToken(token);
        return data;
    }

    public void checkLogin(LoginData data) throws Exception {
        String token = data.getToken();
        Map<String, Object> claims = JwtUtil.parseJWT(token);
        String role = (String) claims.get("role");
        String name = (String) claims.get("name");
        if (!role.equals(data.getRole()) || !name.equals(data.getName())) {
            throw new Exception("用户登录信息被篡改！");
        }
    }
}
