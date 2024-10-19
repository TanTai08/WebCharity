package com.example.SGUCharity_Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login_admin_controller {
    @GetMapping("/login")
    public String logout() {
        return "account_admin/Signln";
    }
}
