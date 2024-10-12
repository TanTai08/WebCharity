package com.example.SGUCharity_Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FundraisingCampaign_controller {
    @GetMapping("/chien-dich-gay-quy")
    public String fundraisingcampaign() {
        return "page_user/FundraisingCampaign";
    }
}
