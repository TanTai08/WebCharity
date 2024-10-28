package com.example.SGUCharity_Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Contribute_controller {
    @GetMapping("/huong-dan-dong-gop")
    public String contribute() {
        return "page_user/Contribute";
    }

    @GetMapping("/cap-nhat-dong-gop")
    public String contributionupdate() {
        return "page_user/ContributionUpdate";
    }
}
