package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Artical_model;
import com.example.SGUCharity_Project.Model.Articaldetail_model;
import com.example.SGUCharity_Project.Model.Communitynews_model;
import com.example.SGUCharity_Project.Model.Payment_model;
import com.example.SGUCharity_Project.Repository.ArticalDetail_Repo;
import com.example.SGUCharity_Project.Repository.Charitycontent_Repo;
import com.example.SGUCharity_Project.Repository.CommunityNews_Repo;
import com.example.SGUCharity_Project.Repository.Payment_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class Dashboard_controller {
    @Autowired
    Charitycontent_Repo charitycontentRepo;

    @Autowired
    ArticalDetail_Repo articalDetailRepo;

    @Autowired
    Payment_Repo paymentRepo;

    @Autowired
    CommunityNews_Repo communityNewsRepo;

    // Render ra trang dashboard
    @GetMapping("/manager")
    public String dashboard() {
        return "page_admin/Dashboard";
    }

    // Render ra trang quản lý chương trình
    @GetMapping("/dashboard_programmanagement")
    public String programmanagement(Model model) {
        List<Artical_model> charitycontentModelList = charitycontentRepo.findAll();
        model.addAttribute("charitycontentModelLists", charitycontentModelList);
        return "page_admin/ProgramManagement_admin";
    }

    // Render ra trang quản lý nội dung bài viết
    @GetMapping("/dashboard_articlemanagement")
    public String articlemanagemrnt(Model model) {
        List<Articaldetail_model> articaldetailModelList = articalDetailRepo.findAll();
        model.addAttribute("articaldetailModelLists", articaldetailModelList);
        return "page_admin/ArticleManagement_admin";
    }

    @GetMapping("/insert/program")
    public String insert_program() {
        return "page_admin/CRUD_ProgramManagement/insertProgram";
    }

    // Handle chuc nang insert account
    @PostMapping("/insert/program")
    public String insertProgram(@RequestParam("inputimg") String inputimg, @RequestParam("inputtitle") String inputtitle, @RequestParam("content1") String content1,  // Nội dung chi tiết 1
                                @RequestParam("imgContent") String imgContent,  // Ảnh chi tiết
                                @RequestParam("content2") String content2) {  // Nội dung chi tiết 2

        // Tạo một Artical_model mới và lưu nó
        Artical_model artical = new Artical_model();
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStatus("Đang vận động");
        artical.setDisplaycategory("Mặc định");

        // Lưu Artical_model vào database để có ID
        Artical_model savedArtical = charitycontentRepo.save(artical);

        // Tạo một Articaldetail_model mới và liên kết với Artical_model đã lưu
        Articaldetail_model articalDetail = new Articaldetail_model();
        articalDetail.setContent_1(content1);
        articalDetail.setImg_content(imgContent);
        articalDetail.setContent_2(content2);

        // Liên kết với bài viết chính
        articalDetail.setArtical(savedArtical);

        // Lưu Articaldetail_model vào database
        articalDetailRepo.save(articalDetail);

        return "redirect:/insert/program";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        // Tìm đối tượng charitycontentModelList bằng id
        Optional<Artical_model> charityContentOptional = charitycontentRepo.findById(id);
        if (charityContentOptional.isPresent()) {
            Artical_model charityContent = charityContentOptional.get();
            charityContent.setStatus(status); // Cập nhật status
            charitycontentRepo.save(charityContent); // Lưu lại thay đổi
        }
        return "redirect:/dashboard_programmanagement"; // Chuyển hướng về trang mà bạn muốn
    }

    @PostMapping("/updateDisplayCategory")
    public String updateDisplayCategory(@RequestParam("id") Long id, @RequestParam("display") String display) {
        Optional<Artical_model> charityContentOptional = charitycontentRepo.findById(id);
        if (charityContentOptional.isPresent()) {
            Artical_model charityContent = charityContentOptional.get();
            charityContent.setDisplaycategory(display);
            charitycontentRepo.save(charityContent);
        }
        return "redirect:/dashboard_programmanagement";
    }

    @GetMapping("dashboard_revenuemanagement")
    public String revenue(Model model) {
        List<Payment_model> paymentModels = paymentRepo.findAll();
        model.addAttribute("paymentModel",paymentModels);
        return "page_admin/RevenueManagement_admin";
    }

    @PostMapping("/displayrevenue")
    public String displayrevenue(@RequestParam("id") Long id, @RequestParam("display") int display, Model model) {
        // Tìm đối tượng Payment_model theo ID
        Optional<Payment_model> paymentModelOp = paymentRepo.findById(id);

        if (paymentModelOp.isPresent()) {
            Payment_model paymentModel = paymentModelOp.get();
            paymentModel.setDisplay(display);
            paymentRepo.save(paymentModel);
        }
        return "redirect:/dashboard_revenuemanagement";
    }

    // Handle chuc nang delete revenue
    @PostMapping("revenue/delete/{id_delete}")
    public String revenue_delete(@PathVariable("id_delete") Long id) {
        paymentRepo.deleteById(id);
        return "redirect:/dashboard_revenuemanagement";
    }

    @GetMapping("dashboard_newsmanagement")
    public String newsmanagement(Model model) {
        List<Communitynews_model> communityNewsModels = communityNewsRepo.findAll();
        model.addAttribute("communityNewsModels", communityNewsModels);
        return "page_admin/NewsManagement_admin";
    }

    @GetMapping("newsmanagement/{id_update}")
    public String newsmanagement_update(@PathVariable("id_update") Long id, Model model) {
        Communitynews_model communitynewsModel = communityNewsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        model.addAttribute("communitynewsModel", communitynewsModel);
        return "page_admin/CRUD_NewsManagement/updateNews";
    }

    @PostMapping("newsmanagement/{id_update}")
    public String handle_newsmanagement_update(@RequestParam("inputtitlenews") String inputtitlenews, @RequestParam("inputimgnews") String inputimgnews, @RequestParam("inputsubtitlenews") String inputsubtitlenews,
                                 @RequestParam("inputurlartical") String inputurlartical, @PathVariable("id_update") Long id) {
        Communitynews_model communitynewsModel = communityNewsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        return "redirect:/dashboard_newsmanagement";
    }

    @PostMapping("newsmanagement/delete/{id_delete}")
    public String newsmanagement_delete(@PathVariable("id_delete") Long id) {
        communityNewsRepo.deleteById(id);
        return "redirect:/dashboard_newsmanagement";
    }

    @GetMapping("/insert/news")
    public String insertnews() {
        return "page_admin/CRUD_NewsManagement/insertNews";
    }

    @PostMapping("/insert/news")
    public String insertnews(@RequestParam("inputtitlenews") String inputtitlenews, @RequestParam("inputimgnews") String inputimgnews, @RequestParam("inputsubtitlenews") String inputsubtitlenews,
                                @RequestParam("inputurlartical") String inputurlartical) {

        Communitynews_model communitynewsModel = new Communitynews_model();
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        return "redirect:/dashboard_newsmanagement";
    }
}
