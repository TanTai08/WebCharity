package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Communitynews_model;
import com.example.SGUCharity_Project.Repository.CommunityNews_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CommunityNews_controller {
    @Autowired
    CommunityNews_Repo communityNewsRepo;

    @GetMapping("/tin-tuc-cong-dong")
    public String cmmunitynews(Model model) {
        List<Communitynews_model> communityNewsModels = communityNewsRepo.findAll();
        model.addAttribute("communityNewsModels", communityNewsModels);
        return "page_user/CommunityNews";
    }
}
