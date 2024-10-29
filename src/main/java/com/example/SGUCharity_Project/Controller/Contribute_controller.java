package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Payment_model;
import com.example.SGUCharity_Project.Repository.Payment_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Contribute_controller {
    @Autowired
    Payment_Repo paymentRepo;

    @GetMapping("/huong-dan-dong-gop")
    public String contribute() {
        return "page_user/Contribute";
    }

    @GetMapping("/cap-nhat-dong-gop")
    public String contributionupdate(Model model) {
        List<Payment_model> paymentModels = paymentRepo.findByDisplay(1); // Lấy các payment có display = 1
        model.addAttribute("paymentModelUpdate", paymentModels);
        return "page_user/ContributionUpdate";
    }
}
