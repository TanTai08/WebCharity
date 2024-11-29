package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Artical_model;
import com.example.SGUCharity_Project.Model.Articaldetail_model;
import com.example.SGUCharity_Project.Model.Service_model;
import com.example.SGUCharity_Project.Model.Statistical_model;
import com.example.SGUCharity_Project.Repository.ArticalDetail_Repo;
import com.example.SGUCharity_Project.Repository.Charitycontent_Repo;
import com.example.SGUCharity_Project.Repository.ServiceOperations_Repo;
import com.example.SGUCharity_Project.Repository.Statistical_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class Home_controller {
    @Autowired
    private Charitycontent_Repo charitycontentRepo;

    @Autowired
    private ArticalDetail_Repo articalDetailRepo;

    @Autowired
    private Statistical_Repo statisticalRepo;

    @Autowired
    ServiceOperations_Repo serviceOperationsRepo;

    // Render ra trang product
    @GetMapping("/trang-chu")
    public String list_content(Model model) {
        List<Artical_model> charitycontent = charitycontentRepo.findAll();
        List<Statistical_model> statisticalModels = statisticalRepo.findAll();
        List<Service_model> serviceModels = serviceOperationsRepo.findAll();

        // Kiểm tra nếu danh sách không rỗng
        if (!statisticalModels.isEmpty()) {
            model.addAttribute("statisticalModel", statisticalModels.get(statisticalModels.size() - 1)); // Lấy đối tượng cuối cùng
        } else {
            model.addAttribute("statisticalModel", new Statistical_model()); // Gán đối tượng mới nếu danh sách rỗng
        }

        model.addAttribute("title_home", "title_home");
        model.addAttribute("title_introduce", "title_introduce");
        model.addAttribute("title_donationcontext", "title_donationcontext");
        model.addAttribute("title_fundraisingcampaign", "title_fundraisingcampaign");
        model.addAttribute("title_criticalillness", "title_criticalillness");
        model.addAttribute("title_empoweringstudentstoattendschool", "title_empoweringstudentstoattendschool");
        model.addAttribute("title_studentmeals", "title_studentmeals");
        model.addAttribute("title_excellentstudentsfacinghardships", "title_excellentstudentsfacinghardships");
        model.addAttribute("title_contribute", "title_contribute");
        model.addAttribute("title_contributionupdates", "title_contributionupdates");
        model.addAttribute("title_donationguidelines", "title_donationguidelines");
        model.addAttribute("title_communitynews", "title_communitynews");
        model.addAttribute("title_contact", "title_contact");
        model.addAttribute("title_language", "title_language");
        model.addAttribute("Kindnessalwaysbringsaboutmiracles", "Kindnessalwaysbringsaboutmiracles");
        model.addAttribute("Donation", "Donation");
        model.addAttribute("KeyProjects", "KeyProjects");
        model.addAttribute("NewlyAnnouncedProjects", "NewlyAnnouncedProjects");
        model.addAttribute("OngoingProjects", "OngoingProjects");
        model.addAttribute("SuccessfulProjects", "SuccessfulProjects");
        model.addAttribute("SeeMoreDetails", "SeeMoreDetails");
        model.addAttribute("Statistics", "Statistics");
        model.addAttribute("NumberofActiveUsers", "NumberofActiveUsers");
        model.addAttribute("AmountDonated", "AmountDonated");
        model.addAttribute("EventsOrganized", "EventsOrganized");
        model.addAttribute("ProgramServiceActivities", "ProgramServiceActivities");
        model.addAttribute("Connectwithusonsocialmedia", "Connectwithusonsocialmedia");
        model.addAttribute("AboutUs", "AboutUs");
        model.addAttribute("Webelievethat", "Webelievethat");
        model.addAttribute("Guide", "Guide");
        model.addAttribute("DonationGuide", "DonationGuide");
        model.addAttribute("Introduction", "Introduction");
        model.addAttribute("Organization", "Organization");
        model.addAttribute("History", "History");
        model.addAttribute("Program", "Program");
        model.addAttribute("EmergencyRelief", "EmergencyRelief");
        model.addAttribute("MedicalAssistance", "MedicalAssistance");
        model.addAttribute("LowcostMeals", "LowcostMeals");
        model.addAttribute("LivelihoodSupport", "LivelihoodSupport");
        model.addAttribute("Contact", "Contact");
        model.addAttribute("SaigonUniversityCharityFund", "SaigonUniversityCharityFund");
        model.addAttribute("ViewMore", "ViewMore");

        model.addAttribute("charitycontent", charitycontent);
        model.addAttribute("serviceModels", serviceModels);

        return "page_user/Home";  // Trả về trang Home.html
    }

    // Hiển thị chi tiết bài viết khi click vào
    @GetMapping("/chuong-trinh/{id}")
    public String showCharityContentDetail(@PathVariable Long id, Model model) {
        Artical_model charityContent = charitycontentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại: " + id));

        // Lấy chi tiết của bài viết
        List<Articaldetail_model> articalDetails = articalDetailRepo.findByartical_id(id);

        model.addAttribute("articalDetails", articalDetails);
        model.addAttribute("charitycontent", charityContent);

        return "page_user/ArticleDetails"; // Trang HTML chứa nội dung chi tiết bài viết
    }

}
