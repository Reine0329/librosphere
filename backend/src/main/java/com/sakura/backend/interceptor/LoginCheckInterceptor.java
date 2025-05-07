package com.sakura.backend.interceptor;

import com.google.gson.Gson;
import com.sakura.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@CrossOrigin
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
        // 拦截器取到请求先进行判断，如果是OPTIONS请求，则放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("Method: OPTIONS");
            return true;
        }

        // 获取请求头中的JWT令牌
        String jwt = request.getHeader("Authorization");

        if (!StringUtils.hasLength(jwt)) {
            log.info("未携带jwt令牌信息");
            promptNotLogin(response, "未携带JWT令牌");
            return false;
        }

        try {
            JwtUtil.parseJWT(jwt);
        } catch (Exception e) {
            log.info("JWT令牌解析失败");
            promptNotLogin(response, "JWT令牌无效");
            return false;
        }

        return true;
    }

    private void promptNotLogin(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(Collections.singletonMap("error", message));

        response.getWriter().write(jsonResponse);
    }
}
