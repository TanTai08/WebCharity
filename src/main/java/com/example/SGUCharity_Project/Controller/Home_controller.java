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
