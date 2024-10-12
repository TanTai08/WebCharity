package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.Artical_model;
import com.example.SGUCharity_Project.Model.Articaldetail_model;
import com.example.SGUCharity_Project.Repository.ArticalDetail_Repo;
import com.example.SGUCharity_Project.Repository.Charitycontent_Repo;
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

    // Render ra trang product
    @GetMapping("/trang-chu")
    public String list_content(Model model) {
        List<Artical_model> charitycontent = charitycontentRepo.findAll();
        model.addAttribute("charitycontent", charitycontent);
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
