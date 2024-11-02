package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.FundraisingCampaign_model;
import com.example.SGUCharity_Project.Repository.FundraisingCampaign_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FundraisingCampaign_controller {

    @Autowired
    private FundraisingCampaign_Repo fundraisingCampaignRepo;

    @GetMapping("/chien-dich-gay-quy")
    public String fundraisingCampaign(Model model) {
        List<FundraisingCampaign_model> campaigns = fundraisingCampaignRepo.findAll();
        model.addAttribute("campaigns", campaigns);
        return "page_user/FundraisingCampaign";
    }
}
