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
    @GetMapping("/benh-hiem-ngheo")
    public String diseaseCampaign(Model model) {
        List<FundraisingCampaign_model> campaigns = fundraisingCampaignRepo.findByCategory("Bệnh hiểm nghèo");
        model.addAttribute("campaigns", campaigns);
        return "page_user/diseaseCampaign"; // Trang cho danh mục Bệnh hiểm nghèo
    }

    @GetMapping("/chap-canh-sinh-vien")
    public String studentSupportCampaign(Model model) {
        List<FundraisingCampaign_model> campaigns = fundraisingCampaignRepo.findByCategory("Chấp cánh sinh viên");
        model.addAttribute("campaigns", campaigns);
        return "page_user/studentSupportCampaign"; // Trang cho danh mục Chấp cánh sinh viên
    }

    @GetMapping("/bua-an-sinh-vien")
    public String mealSupportCampaign(Model model) {
        List<FundraisingCampaign_model> campaigns = fundraisingCampaignRepo.findByCategory("Bữa ăn sinh viên");
        model.addAttribute("campaigns", campaigns);
        return "page_user/mealSupportCampaign"; // Trang cho danh mục Bữa ăn sinh viên
    }

    @GetMapping("/sinh-vien-gioi-co-hoan-canh")
    public String talentedStudentsCampaign(Model model) {
        List<FundraisingCampaign_model> campaigns = fundraisingCampaignRepo.findByCategory("Sinh viên giỏi có hoàn cảnh");
        model.addAttribute("campaigns", campaigns);
        return "page_user/talentedStudentsCampaign"; // Trang cho danh mục Sinh viên giỏi có hoàn cảnh
    }

}
