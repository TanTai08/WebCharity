package com.example.SGUCharity_Project.Config;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Kiểm tra nếu người dùng chưa đăng nhập (session không chứa "username")
        if (request.getSession().getAttribute("username") == null) {
            response.sendRedirect("/login-siteadmin");  // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
            return false; // Dừng xử lý request
        }
        return true; // Cho phép xử lý request nếu đã đăng nhập
    }
}
