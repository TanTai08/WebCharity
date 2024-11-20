package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Authorization_model;
import com.example.SGUCharity_Project.Repository.Authorization_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class Authorization_controller {
    @Autowired
    private Authorization_Repo authorizationRepo;

    @GetMapping("/login-siteadmin")
    public String loginPage() {
        return "page_admin/Login_View_Admin";  // Hiển thị trang đăng nhập
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        // Tìm user trong CSDL
        Authorization_model user = authorizationRepo.findByUsername(username).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            // Lưu thông tin người dùng và quyền vào session
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRoles());
            return "redirect:/manager"; // Chuyển hướng đến trang dashboard
        } else {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "page_admin/Login_View_Admin";  // Nếu sai, quay lại trang đăng nhập
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Xóa session khi đăng xuất
        return "redirect:/login-siteadmin";  // Chuyển hướng về trang đăng nhập
    }

}
