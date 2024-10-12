package com.example.SGUCharity_Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Contribute_controller {
    @GetMapping("/dong-gop")
    public String contribute() {
        return "page_user/Contribute";
    }
}
