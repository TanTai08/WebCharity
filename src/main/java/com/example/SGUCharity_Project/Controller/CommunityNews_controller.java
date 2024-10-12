package com.example.SGUCharity_Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityNews_controller {
    @GetMapping("/tin-tuc-cong-dong")
    public String cmmunitynews() {
        return "page_user/CommunityNews";
    }
}
