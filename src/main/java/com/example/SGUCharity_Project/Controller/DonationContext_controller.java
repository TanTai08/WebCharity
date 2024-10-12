package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Artical_model;
import com.example.SGUCharity_Project.Repository.Charitycontent_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DonationContext_controller {
    @Autowired
    private Charitycontent_Repo charitycontentRepo;

    @GetMapping("/chuong-trinh")
    public String donationcontext(Model model) {
        List<Artical_model> charitycontent = charitycontentRepo.findAll();
        model.addAttribute("charitycontent", charitycontent);
        return "page_user/DonationContext";
    }
}
