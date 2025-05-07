package com.sakura.backend.controller;

import com.sakura.backend.model.LoginData;
import com.sakura.backend.model.LoginParam;
import com.sakura.backend.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginParam json) {
        log.info(json.toString());
        LoginData data = loginService.login(json);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败！");
        }
        return ResponseEntity.ok(data);
    }

    // 检查用户登录
    @PostMapping("/check/login")
    public ResponseEntity<?> checkLogin(@RequestBody LoginData data) {
        try {
            loginService.checkLogin(data);
            return ResponseEntity.ok("登录状态有效");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // 异常处理
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleRuntimeException(RuntimeException e) {
        return e.getMessage();
    }
}
