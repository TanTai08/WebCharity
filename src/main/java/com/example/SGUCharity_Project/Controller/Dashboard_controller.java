package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.*;
import com.example.SGUCharity_Project.Repository.*;

import com.example.SGUCharity_Project.Service.EmailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;



@Controller
public class Dashboard_controller {
    @Autowired
    Charitycontent_Repo charitycontentRepo;

    @Autowired
    ArticalDetail_Repo articalDetailRepo;

    @Autowired
    FundraisingCampaign_Repo fundraisingCampaignRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    Payment_Repo paymentRepo;

    @Autowired
    CommunityNews_Repo communityNewsRepo;

    @Autowired
    Contact_Repo contactRepo;

    @Autowired
    Note_Repo noteRepo;

    @Autowired
    Statistical_Repo statisticalRepo;

    @Autowired
    ServiceOperations_Repo serviceOperationsRepo;

    @Autowired
    Authorization_Repo authorizationRepo;

    @Autowired
    Activity_Repo activityRepo;

    @Autowired
    private EmailService emailService;

    // Render ra trang dashboar
    @GetMapping("/manager")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra nếu người dùng chưa đăng nhập (session không chứa username)
        if (session.getAttribute("username") == null) {
            return "redirect:/login-siteadmin"; // Chuyển hướng đến trang đăng nhập
        }

        // Lấy role từ session
        String role = (String) session.getAttribute("role");

        // Chuyển hướng dựa trên role
        if ("admin".equals(role)) {
            model.addAttribute("role", "admin");
        } else if ("staff".equals(role)) {
            model.addAttribute("role", "staff");
        }

        return "page_admin/Dashboard";
    }


    @GetMapping("/dashboard_programmanagement")
    public String getDashboard(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artical_model> resultPage;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                // Tìm kiếm theo ID
                Long id = Long.valueOf(searchTerm);
                resultPage = charitycontentRepo.searchById(id, pageable);
            } catch (NumberFormatException e) {
                // Tìm kiếm theo tiêu đề
                resultPage = charitycontentRepo.searchByTitle(searchTerm, pageable);
            }
        } else {
            resultPage = charitycontentRepo.findAll(pageable);
        }

        model.addAttribute("charitycontentModelLists", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("searchTerm", searchTerm);
        return "page_admin/ProgramManagement_admin";
    }


    // Render ra trang quản lý nội dung bài viết
    @GetMapping("/dashboard_articlemanagement")
    public String articlemanagement(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<Articaldetail_model> pageResult = articalDetailRepo.findAll(pageable);

        model.addAttribute("articaldetailModelLists", pageResult.getContent()); // Danh sách các mục của trang hiện tại
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", pageResult.getTotalElements()); // Tổng số mục

        return "page_admin/ArticleManagement_admin";
    }

    @PostMapping("program/delete/{id_delete}")
    public String program_delete(@PathVariable("id_delete") Long id, HttpSession session) {
        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Xóa");
        activityModel.setDetail_activity("Xóa bài viết chương trình với ID: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        charitycontentRepo.deleteById(id);
        return "redirect:/dashboard_programmanagement";
    }

    @GetMapping("/insert/program")
    public String insert_program() {
        return "page_admin/CRUD_ProgramManagement/insertProgram";
    }

    // Handle chuc nang insert account
    @PostMapping("/insert/program")
    public String insertProgram(
            @RequestParam("inputimg") String inputimg,
            @RequestParam("inputtitle") String inputtitle,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("goalAmount") String goalAmount,
            @RequestParam("content1") String content1,
            @RequestParam("content2") String content2,
            @RequestParam("content3") String content3,
            @RequestParam("imgContent") String imgContent,
            @RequestParam("imgContent2") String imgContent2,
            HttpSession session,
            @RequestParam("code") String code,
            Model model) {

        // Kiểm tra dữ liệu đầu vào
        if (inputimg.isBlank() || inputtitle.isBlank() || startDate.isBlank() || endDate.isBlank() ||
                goalAmount.isBlank() || content1.isBlank() || content2.isBlank() || content3.isBlank() ||
                imgContent.isBlank() || imgContent2.isBlank() || code.isBlank()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            // Giữ lại các giá trị đã nhập
            model.addAttribute("inputtitle", inputtitle);
            model.addAttribute("inputimg", inputimg);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("content1", content1);
            model.addAttribute("content2", content2);
            model.addAttribute("content3", content3);
            model.addAttribute("imgContent", imgContent);
            model.addAttribute("imgContent2", imgContent2);
            model.addAttribute("code", code);
            return "page_admin/CRUD_ProgramManagement/insertProgram";
        }

        // Kiểm tra tính hợp lệ của ngày
        LocalDate start, end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
            if (start.isAfter(end)) {
                model.addAttribute("error", "Ngày bắt đầu không thể sau ngày kết thúc.");
                // Giữ lại các giá trị đã nhập
                model.addAttribute("inputimg", inputimg);
                model.addAttribute("inputtitle", inputtitle);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("goalAmount", goalAmount);
                model.addAttribute("content1", content1);
                model.addAttribute("content2", content2);
                model.addAttribute("content3", content3);
                model.addAttribute("imgContent", imgContent);
                model.addAttribute("imgContent2", imgContent2);
                model.addAttribute("code", code);
                return "page_admin/CRUD_ProgramManagement/insertProgram";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày không hợp lệ. Định dạng phải là yyyy-MM-dd.");
            // Giữ lại các giá trị đã nhập
            model.addAttribute("inputimg", inputimg);
            model.addAttribute("inputtitle", inputtitle);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("content1", content1);
            model.addAttribute("content2", content2);
            model.addAttribute("content3", content3);
            model.addAttribute("imgContent", imgContent);
            model.addAttribute("imgContent2", imgContent2);
            model.addAttribute("code", code);
            return "page_admin/CRUD_ProgramManagement/insertProgram";
        }

        // Kiểm tra tính hợp lệ của số tiền
        double goal;
        try {
            goal = Double.parseDouble(goalAmount);
            if (goal <= 0) {
                model.addAttribute("error", "Số tiền phải lớn hơn 0.");
                // Giữ lại các giá trị đã nhập
                model.addAttribute("inputimg", inputimg);
                model.addAttribute("inputtitle", inputtitle);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("goalAmount", goalAmount);
                model.addAttribute("content1", content1);
                model.addAttribute("content2", content2);
                model.addAttribute("content3", content3);
                model.addAttribute("imgContent", imgContent);
                model.addAttribute("imgContent2", imgContent2);
                model.addAttribute("code", code);
                return "page_admin/CRUD_ProgramManagement/insertProgram";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Số tiền không hợp lệ.");
            // Giữ lại các giá trị đã nhập
            model.addAttribute("inputimg", inputimg);
            model.addAttribute("inputtitle", inputtitle);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("content1", content1);
            model.addAttribute("content2", content2);
            model.addAttribute("content3", content3);
            model.addAttribute("imgContent", imgContent);
            model.addAttribute("imgContent2", imgContent2);
            model.addAttribute("code", code);
            return "page_admin/CRUD_ProgramManagement/insertProgram";
        }

        // Kiểm tra mã code đã tồn tại
        boolean codeExists = !charitycontentRepo.findByCode(code).isEmpty();
        if (codeExists) {
            model.addAttribute("error", "Mã chương trình đã tồn tại, vui lòng nhập mã khác.");
            // Giữ lại các giá trị đã nhập
            model.addAttribute("inputimg", inputimg);
            model.addAttribute("inputtitle", inputtitle);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("content1", content1);
            model.addAttribute("content2", content2);
            model.addAttribute("content3", content3);
            model.addAttribute("imgContent", imgContent);
            model.addAttribute("imgContent2", imgContent2);
            model.addAttribute("code", code);
            return "page_admin/CRUD_ProgramManagement/insertProgram";
        }

        // Tạo và lưu Artical_model
        Artical_model artical = new Artical_model();
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStartDate(start);
        artical.setEndDate(end);
        artical.setGoalAmount(goal);
        artical.setAmountRaised(0); // Initialize với 0
        artical.setCode(code);
        artical.setStatus("Active");
        artical.setDisplaycategory("Default");

        Artical_model savedArtical = charitycontentRepo.save(artical);

        // Tạo và lưu Articaldetail_model
        Articaldetail_model articalDetail = new Articaldetail_model();
        articalDetail.setContent_1(content1);
        articalDetail.setContent_2(content2);
        articalDetail.setContent_3(content3);
        articalDetail.setImg_content(imgContent);
        articalDetail.setImg_content2(imgContent2);
        articalDetail.setArtical(savedArtical);

        articalDetailRepo.save(articalDetail);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");

        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username != null ? username : "Unknown");
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm một chương trình mới");
        activityModel.setDatetime(LocalDateTime.now());

        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_programmanagement";
    }

    @GetMapping("programmanagement/{id_update}")
    public String programmanagement_update(@PathVariable("id_update") Long id, Model model) {
        Artical_model articalModel = charitycontentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID " + id));
        Articaldetail_model articaldetailModels = articalDetailRepo.findFirstByArtical_Id(id);
        model.addAttribute("articalModel", articalModel);
        model.addAttribute("articaldetailModels", articaldetailModels);

        return "page_admin/CRUD_ProgramManagement/updateProgram";
    }

    @PostMapping("programmanagement/{id_update}")
    public String handle_programmanagement_update(
            @PathVariable("id_update") Long id,
            @RequestParam("img") String inputimg,
            @RequestParam("title") String inputtitle,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("goalAmount") String goalAmount,
            @RequestParam("content1") String content1,
            @RequestParam("content2") String content2,
            @RequestParam("content3") String content3,
            @RequestParam("imgContent") String imgContent,
            @RequestParam("imgContent2") String imgContent2,
            HttpSession session, @RequestParam("code") String code,
            Model model) {

        // Lấy bài viết hiện tại
        Artical_model artical = charitycontentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + id));
        Articaldetail_model articalDetail = articalDetailRepo.findFirstByArtical_Id(id);

        // Kiểm tra dữ liệu đầu vào
        if (inputimg.isBlank() || inputtitle.isBlank() || startDate.isBlank() || endDate.isBlank() ||
                goalAmount.isBlank() || content1.isBlank() || content2.isBlank() || content3.isBlank() ||
                imgContent.isBlank() || imgContent2.isBlank() || code.isBlank()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
            model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
            return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
        }

        // Kiểm tra tính hợp lệ của ngày
        LocalDate start, end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
            if (start.isAfter(end)) {
                model.addAttribute("error", "Ngày bắt đầu không thể sau ngày kết thúc.");
                model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
                model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
                return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày không hợp lệ. Định dạng phải là yyyy-MM-dd.");
            model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
            model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
            return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
        }

        // Kiểm tra tính hợp lệ của số tiền
        double goal;
        try {
            goal = Double.parseDouble(goalAmount);
            if (goal <= 0) {
                model.addAttribute("error", "Số tiền phải lớn hơn 0.");
                model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
                model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
                return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Số tiền không hợp lệ.");
            model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
            model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
            return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
        }

        // Kiểm tra mã code không bị trùng (không tính bài viết hiện tại)
        boolean codeExists = charitycontentRepo.findByCode(code).stream()
                .anyMatch(existingArtical -> !existingArtical.getId().equals(id));
        if (codeExists) {
            model.addAttribute("error", "Mã bài viết đã tồn tại, vui lòng nhập mã khác.");
            model.addAttribute("articalModel", artical); // Giữ lại dữ liệu bài viết hiện tại
            model.addAttribute("articaldetailModels", articalDetail); // Giữ lại dữ liệu chi tiết bài viết hiện tại
            return "page_admin/CRUD_ProgramManagement/updateProgram"; // Trả về trang chỉnh sửa
        }

        // Cập nhật thông tin bài viết
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStartDate(start);
        artical.setEndDate(end);
        artical.setGoalAmount(goal);
        artical.setCode(code);
        charitycontentRepo.save(artical);

        // Cập nhật thông tin chi tiết bài viết
        articalDetail.setContent_1(content1);
        articalDetail.setContent_2(content2);
        articalDetail.setContent_3(content3);
        articalDetail.setImg_content(imgContent);
        articalDetail.setImg_content2(imgContent2);
        articalDetailRepo.save(articalDetail);

        // Ghi log hoạt động
        String username = (String) session.getAttribute("username");
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username != null ? username : "Unknown");
        activityModel.setActivity("Sửa");
        activityModel.setDetail_activity("Sửa bài viết chương trình với ID: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        activityRepo.save(activityModel);

        return "redirect:/dashboard_programmanagement";
    }


    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        charitycontentRepo.findById(id).ifPresent(artical -> {
            artical.setStatus(status);
            charitycontentRepo.save(artical);
        });
        return "redirect:/dashboard_programmanagement";
    }



    @PostMapping("/updateDisplayCategory")
    public String updateDisplayCategory(@RequestParam("id") Long id, @RequestParam("display") String display) {
        charitycontentRepo.findById(id).ifPresent(artical -> {
            artical.setDisplaycategory(display);
            if ("5".equals(display)) { // Kiểm tra nếu là "Các dự án thành công"
                artical.setStatus("1"); // Tự động chuyển trạng thái thành "Đã đóng"
            }
            charitycontentRepo.save(artical);
        });
        return "redirect:/dashboard_programmanagement";
    }


    @GetMapping("/dashboard_revenuemanagement")
    public String revenue(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment_model> resultPage;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                // Tìm kiếm theo ID (ID có thể là một số)
                Long id = Long.valueOf(searchTerm);
                resultPage = paymentRepo.searchById(id, pageable);
            } catch (NumberFormatException e) {
                // Tìm kiếm theo nội dung chuyển khoản hoặc trường hợp tìm kiếm khác
                resultPage = paymentRepo.searchByOrderId(searchTerm, pageable);
            }
        } else {
            resultPage = paymentRepo.findAll(pageable);
        }

        model.addAttribute("paymentModel", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        model.addAttribute("searchTerm", searchTerm);

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

    @GetMapping("/dashboard_newsmanagement")
    public String newsManagement(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<Communitynews_model> pageResult;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                // Thử chuyển searchTerm thành Long để tìm kiếm theo ID
                Long id = Long.valueOf(searchTerm);
                pageResult = communityNewsRepo.searchById(id, pageable); // Tìm kiếm theo ID
            } catch (NumberFormatException e) {
                // Nếu không chuyển được, tìm kiếm theo tiêu đề
                pageResult = communityNewsRepo.searchByTitle(searchTerm, pageable);
            }
        } else {
            pageResult = communityNewsRepo.findAll(pageable); // Nếu không có từ khóa, lấy tất cả
        }

        // Thêm dữ liệu vào model
        model.addAttribute("communityNewsModels", pageResult.getContent()); // Danh sách bài viết
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("searchTerm", searchTerm); // Từ khóa tìm kiếm

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
                                               @RequestParam("inputurlartical") String inputurlartical, @PathVariable("id_update") Long id, HttpSession session) {
        Communitynews_model communitynewsModel = communityNewsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Cập nhật");
        activityModel.setDetail_activity("Sửa 1 bài viết tin tức cộng đồng: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_newsmanagement";
    }

    @PostMapping("newsmanagement/delete/{id_delete}")
    public String newsmanagement_delete(@PathVariable("id_delete") Long id, HttpSession session) {
        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Xóa");
        activityModel.setDetail_activity("Xóa 1 bài viết tin tức cộng đồng" + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        communityNewsRepo.deleteById(id);
        return "redirect:/dashboard_newsmanagement";
    }

    @GetMapping("/insert/news")
    public String insertnews() {
        return "page_admin/CRUD_NewsManagement/insertNews";
    }

    @PostMapping("/insert/news")
    public String insertnews(@RequestParam("inputtitlenews") String inputtitlenews, @RequestParam("inputimgnews") String inputimgnews, @RequestParam("inputsubtitlenews") String inputsubtitlenews,
                             @RequestParam("inputurlartical") String inputurlartical, HttpSession session) {

        Communitynews_model communitynewsModel = new Communitynews_model();
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm 1 bài viết tin tức cộng đồng");
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_newsmanagement";
    }


    @GetMapping("/dashboard_servicemanagement")
    public String servicemanagement(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @RequestParam(required = false) String searchTerm,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size); // Set the page and size for pagination
        Page<Service_model> serviceModelsPage;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                // Attempt to convert searchTerm to Long to search by ID
                Long id = Long.valueOf(searchTerm);
                serviceModelsPage = serviceOperationsRepo.findById(id, pageable); // Search by ID
            } catch (NumberFormatException e) {
                // If conversion fails, search by title
                serviceModelsPage = serviceOperationsRepo.findByTitle_serviceContainingIgnoreCase(searchTerm, pageable);
            }
        } else {
            // If no search term is provided, return all records
            serviceModelsPage = serviceOperationsRepo.findAll(pageable);
        }

        // Add the data to the model for rendering in the view
        model.addAttribute("serviceModels", serviceModelsPage.getContent()); // List of service models
        model.addAttribute("currentPage", page); // Current page number
        model.addAttribute("totalPages", serviceModelsPage.getTotalPages()); // Total number of pages
        model.addAttribute("searchTerm", searchTerm); // Preserve the search term in the view

        return "page_admin/ServiceManagement_admin";  // The Thymeleaf template for rendering the view
    }




    @GetMapping("/insert/service")
    public String insertservice() {
        return "page_admin/CRUD_ServiceManagement/insertService";
    }

    @PostMapping("/insert/service")
    public String insertservice(@RequestParam("inputtitleservice") String inputtitleservice, @RequestParam("inputimgservice") String inputimgservice, @RequestParam("inputsubtitleservice") String inputsubtitleservice,
                                @RequestParam("inputurlarticalservice") String inputurlarticalservice, HttpSession session) {

        Service_model serviceModel = new Service_model();
        serviceModel.setTitle_service(inputtitleservice);
        serviceModel.setImg_thumbnail(inputimgservice);
        serviceModel.setSubtitle_service(inputsubtitleservice);
        serviceModel.setUrlartical_service(inputurlarticalservice);

        serviceOperationsRepo.save(serviceModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm 1 bài viết hoạt động dịch vụ");
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);


        return "redirect:/dashboard_servicemanagement";
    }

    @GetMapping("/servicemanagement/{id_update}")
    public String servicemanagement_update(@PathVariable("id_update") Long id, Model model) {
        Service_model serviceModel = serviceOperationsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + id));
        model.addAttribute("serviceModels", serviceModel);
        return "page_admin/CRUD_ServiceManagement/updateService";
    }

    @PostMapping("/servicemanagement/{id_update}")
    public String handle_servicemanagement_update(@RequestParam("inputtitleservice") String inputtitleservice, @RequestParam("inputimgservice") String inputimgservice, @RequestParam("inputsubtitleservice") String inputsubtitleservice,
                                                  @RequestParam("inputurlarticalservice") String inputurlarticalservice, @PathVariable("id_update") Long id, HttpSession session) {
        Service_model serviceModel = serviceOperationsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        serviceModel.setTitle_service(inputtitleservice);
        serviceModel.setImg_thumbnail(inputimgservice);
        serviceModel.setSubtitle_service(inputsubtitleservice);
        serviceModel.setUrlartical_service(inputurlarticalservice);

        serviceOperationsRepo.save(serviceModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Sửa");
        activityModel.setDetail_activity("Sửa 1 bài viết hoạt động dịch vụ: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);


        return "redirect:/dashboard_servicemanagement";
    }

    @PostMapping("servicemanagement/delete/{id_delete}")
    public String servicemanagement_delete(@PathVariable("id_delete") Long id, HttpSession session) {
        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Xóa");
        activityModel.setDetail_activity("Xóa 1 bài viết hoạt động dịch vụ: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        serviceOperationsRepo.deleteById(id);
        return "redirect:/dashboard_servicemanagement";
    }

    @GetMapping("dashboard_contact")
    public String contact(Model model) {
        List<Contact_model> contactModels = contactRepo.findAll();
        model.addAttribute("contactModels",contactModels);
        return "page_admin/ContactManagement_admin";
    }

    @PostMapping("contact/delete/{id_delete}")
    public String contact_delete(@PathVariable("id_delete") Long id) {
        contactRepo.deleteById(id);
        return "redirect:/dashboard_contact";
    }

    @GetMapping("dashboard_note")
    public String note(Model model) {
        List<Note_model> notes = noteRepo.findAll();
        model.addAttribute("notes", notes);
        return "page_admin/NoteManagement_admin";
    }

    @PostMapping("/addNote")
    public String addNote(@RequestParam String date, @RequestParam String content) {
        Note_model note = new Note_model();
        note.setDate(date);
        note.setContent(content);
        noteRepo.save(note);
        return "redirect:/dashboard_note";
    }

    @GetMapping("/note")
    public ResponseEntity<List<Note_model>> getNotesByDate(@RequestParam String date) {
        List<Note_model> notes = noteRepo.findByDate(date);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteRepo.deleteById(id);
        return ResponseEntity.noContent().build(); // Trả về mã 204 No Content
    }

    @GetMapping("dashboard_statistical")
    public String statistical() {
        return "page_admin/StatisticalManagement_admin";
    }

    @PostMapping("/insert/statistical")
    public String insertstatistical(@RequestParam("successfulproject") String successfulproject, @RequestParam("participants") String participants,
                                    @RequestParam("donationamount") String donationamount,
                                    @RequestParam("eventsorganized") String eventsorganized) {

        Statistical_model statisticalModel = new Statistical_model();
        statisticalModel.setSuccessfulproject(successfulproject);
        statisticalModel.setParticipants(participants);
        statisticalModel.setDonationamount(donationamount);
        statisticalModel.setEventsorganized(eventsorganized);
        statisticalModel.setDateupdated(LocalDate.now());

        statisticalRepo.save(statisticalModel);

        return "redirect:/dashboard_statistical";
    }

    //     render ra trang chiến dịch dashboard
    @GetMapping("/dashboard_campaignmanagement")
    public String campaignmanagement(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int size,
                                     Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<FundraisingCampaign_model> pageResult;

        // Nếu có searchTerm, thực hiện tìm kiếm theo id hoặc title
        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                // Thử chuyển searchTerm thành Long nếu có thể (dùng cho tìm kiếm theo ID)
                Long id = Long.valueOf(searchTerm);
                pageResult = fundraisingCampaignRepo.searchById(id, pageable); // Tìm kiếm theo ID
            } catch (NumberFormatException e) {
                // Nếu không thể chuyển được, tìm kiếm theo tiêu đề
                pageResult = fundraisingCampaignRepo.searchByTitle(searchTerm, pageable);
            }
        } else {
            pageResult = fundraisingCampaignRepo.findAll(pageable); // Nếu không có searchTerm, lấy tất cả
        }

        // Thêm các thuộc tính vào model để sử dụng trong view
        model.addAttribute("fundraisingCampaignModel", pageResult.getContent()); // Danh sách các mục của trang hiện tại
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", pageResult.getTotalElements()); // Tổng số mục
        model.addAttribute("searchTerm", searchTerm); // Từ khóa tìm kiếm

        return "page_admin/CampaignManagement_admin";
    }


    @PostMapping("/updateStatusCampaign")
    public String updateStatusCampaign(@RequestParam("id") Long id, @RequestParam("status") String status) {
        // Tìm đối tượng charitycontentModelList bằng id
        Optional<FundraisingCampaign_model> fundraisingCampaignOptional = fundraisingCampaignRepo.findById(id);
        if (fundraisingCampaignOptional.isPresent()) {
            FundraisingCampaign_model fundraisingCampaign = fundraisingCampaignOptional.get();
            fundraisingCampaign.setStatus(status); // Cập nhật status
            fundraisingCampaignRepo.save(fundraisingCampaign); // Lưu lại thay đổi
        }

        return "redirect:/dashboard_campaignmanagement"; // Chuyển hướng về trang mà bạn muốn
    }


    @PostMapping("/updateCategoryCampaign")
    public String updateCategoryCampaign(@RequestParam("id") Long id, @RequestParam("display") String display) {
        Optional<FundraisingCampaign_model> fundraisingCampaignOptional = fundraisingCampaignRepo.findById(id);
        if (fundraisingCampaignOptional.isPresent()) {
            FundraisingCampaign_model fundraisingCampaign = fundraisingCampaignOptional.get();
            fundraisingCampaign.setCategory(display);
            fundraisingCampaignRepo.save(fundraisingCampaign);
        }
        return "redirect:/dashboard_campaignmanagement";
    }
    @GetMapping("/insert/campaign")
    public String insertcampaign() {
        return "page_admin/CRUD_CampaignManagement/insertCampaign";
    }
    @PostMapping("/insert/campaign")
    public String insertCampaign(@RequestParam("title") String title,
                                 @RequestParam("imgUrl") String imgUrl,
                                 @RequestParam("startDate") String startDate,
                                 @RequestParam("endDate") String endDate,
                                 @RequestParam("goalAmount") String goalAmount,
                                 @RequestParam("code") String code,
                                 @RequestParam("detailUrl") String detailUrl,
                                 HttpSession session,
                                 Model model) {

        // Kiểm tra dữ liệu đầu vào
        if (title.isBlank() || imgUrl.isBlank() || startDate.isBlank() || endDate.isBlank() ||
                goalAmount.isBlank() || code.isBlank() || detailUrl.isBlank()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            model.addAttribute("title", title);
            model.addAttribute("imgUrl", imgUrl);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("code", code);
            model.addAttribute("detailUrl", detailUrl);
            return "page_admin/CRUD_CampaignManagement/insertCampaign";
        }

        // Kiểm tra tính hợp lệ của ngày
        LocalDate start, end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
            if (start.isAfter(end)) {
                model.addAttribute("error", "Ngày bắt đầu không thể sau ngày kết thúc.");
                model.addAttribute("title", title);
                model.addAttribute("imgUrl", imgUrl);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("goalAmount", goalAmount);
                model.addAttribute("code", code);
                model.addAttribute("detailUrl", detailUrl);
                return "page_admin/CRUD_CampaignManagement/insertCampaign";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày không hợp lệ. Định dạng phải là yyyy-MM-dd.");
            model.addAttribute("title", title);
            model.addAttribute("imgUrl", imgUrl);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("code", code);
            model.addAttribute("detailUrl", detailUrl);
            return "page_admin/CRUD_CampaignManagement/insertCampaign";
        }

        // Kiểm tra tính hợp lệ của số tiền
        double goal;
        try {
            goal = Double.parseDouble(goalAmount);
            if (goal <= 0) {
                model.addAttribute("error", "Số tiền phải lớn hơn 0.");
                model.addAttribute("title", title);
                model.addAttribute("imgUrl", imgUrl);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("goalAmount", goalAmount);
                model.addAttribute("code", code);
                model.addAttribute("detailUrl", detailUrl);
                return "page_admin/CRUD_CampaignManagement/insertCampaign";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Số tiền không hợp lệ.");
            model.addAttribute("title", title);
            model.addAttribute("imgUrl", imgUrl);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("code", code);
            model.addAttribute("detailUrl", detailUrl);
            return "page_admin/CRUD_CampaignManagement/insertCampaign";
        }

        // Kiểm tra mã code đã tồn tại
        boolean codeExists = !fundraisingCampaignRepo.findByCode(code).isEmpty();
        if (codeExists) {
            model.addAttribute("error", "Mã chiến dịch đã tồn tại, vui lòng nhập mã khác.");
            model.addAttribute("title", title);
            model.addAttribute("imgUrl", imgUrl);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("goalAmount", goalAmount);
            model.addAttribute("code", code);
            model.addAttribute("detailUrl", detailUrl);
            return "page_admin/CRUD_CampaignManagement/insertCampaign";
        }

        // Tạo một đối tượng mới và thiết lập các thuộc tính
        FundraisingCampaign_model fundraisingCampaignModel = new FundraisingCampaign_model();
        fundraisingCampaignModel.setTitle(title);
        fundraisingCampaignModel.setImgUrl(imgUrl);
        fundraisingCampaignModel.setStartDate(start);
        fundraisingCampaignModel.setEndDate(end);
        fundraisingCampaignModel.setAmountRaised(0);
        fundraisingCampaignModel.setGoalAmount(goal);
        fundraisingCampaignModel.setCode(code);
        fundraisingCampaignModel.setStatus("Đang vận động");
        fundraisingCampaignModel.setDetailUrl(detailUrl);
        fundraisingCampaignModel.setCategory("Bệnh hiểm nghèo");

        // Lưu vào database
        fundraisingCampaignRepo.save(fundraisingCampaignModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");

        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username != null ? username : "Unknown");
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm một chiến dịch mới");
        activityModel.setDatetime(LocalDateTime.now());

        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_campaignmanagement"; // Điều hướng về trang quản lý chiến dịch
    }


    @GetMapping("campaignmanagement/{id_update}")
    public String campaignmanagement_update(@PathVariable("id_update") Long id, Model model) {
        FundraisingCampaign_model fundraisingCampaignModel = fundraisingCampaignRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID " + id));
        model.addAttribute("fundraisingCampaignModel", fundraisingCampaignModel);
        return "page_admin/CRUD_CampaignManagement/updateCampaign";
    }

    @PostMapping("campaignmanagement/{id_update}")
    public String handleCampaignManagementUpdate(@RequestParam("title") String title,
                                                 @RequestParam("imgUrl") String imgUrl,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate,
                                                 @RequestParam("goalAmount") String goalAmount,
                                                 @RequestParam("code") String code,
                                                 @RequestParam("detailUrl") String detailUrl,
                                                 @PathVariable("id_update") Long id,
                                                 HttpSession session,
                                                 Model model) {
        // Lấy chiến dịch hiện tại
        FundraisingCampaign_model campaign = fundraisingCampaignRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chiến dịch không tồn tại với ID: " + id));

        // Kiểm tra dữ liệu đầu vào
        if (title.isBlank() || imgUrl.isBlank() || startDate.isBlank() || endDate.isBlank() ||
                goalAmount.isBlank() || code.isBlank() || detailUrl.isBlank()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            model.addAttribute("fundraisingCampaignModel", campaign);
            return "page_admin/CRUD_CampaignManagement/updateCampaign";
        }

        // Kiểm tra tính hợp lệ của ngày
        LocalDate start, end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
            if (start.isAfter(end)) {
                model.addAttribute("error", "Ngày bắt đầu không thể sau ngày kết thúc.");
                model.addAttribute("fundraisingCampaignModel", campaign);
                return "page_admin/CRUD_CampaignManagement/updateCampaign";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày không hợp lệ. Định dạng phải là yyyy-MM-dd.");
            model.addAttribute("fundraisingCampaignModel", campaign);
            return "page_admin/CRUD_CampaignManagement/updateCampaign";
        }

        // Kiểm tra tính hợp lệ của số tiền
        double goal;
        try {
            goal = Double.parseDouble(goalAmount);
            if (goal <= 0) {
                model.addAttribute("error", "Số tiền phải lớn hơn 0.");
                model.addAttribute("fundraisingCampaignModel", campaign);
                return "page_admin/CRUD_CampaignManagement/updateCampaign";
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Số tiền không hợp lệ.");
            model.addAttribute("fundraisingCampaignModel", campaign);
            return "page_admin/CRUD_CampaignManagement/updateCampaign";
        }

        // Kiểm tra mã code trùng lặp (ngoại trừ ID hiện tại)
        boolean codeExists = fundraisingCampaignRepo.findByCode(code).stream()
                .anyMatch(c -> !c.getId().equals(id));
        if (codeExists) {
            model.addAttribute("error", "Mã chiến dịch đã tồn tại, vui lòng nhập mã khác.");
            model.addAttribute("fundraisingCampaignModel", campaign);
            return "page_admin/CRUD_CampaignManagement/updateCampaign";
        }

        // Cập nhật các thuộc tính của chiến dịch
        campaign.setTitle(title);
        campaign.setImgUrl(imgUrl);
        campaign.setStartDate(start);
        campaign.setEndDate(end);
        campaign.setGoalAmount(goal);
        campaign.setDetailUrl(detailUrl);
        campaign.setCode(code);

        // Lưu chiến dịch đã cập nhật
        fundraisingCampaignRepo.save(campaign);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");

        // Ghi log hoạt động
        Activity_model activity = new Activity_model();
        activity.setUsername(username != null ? username : "Unknown");
        activity.setActivity("Cập nhật");
        activity.setDetail_activity("Cập nhật chiến dịch với ID: " + id);
        activity.setDatetime(LocalDateTime.now());
        activityRepo.save(activity);

        return "redirect:/dashboard_campaignmanagement"; // Redirect tới trang quản lý chiến dịch
    }





    @PostMapping("campaign/delete/{id_delete}")
    public String campaign_delete(@PathVariable("id_delete") Long id, HttpSession session) {
        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Xóa");
        activityModel.setDetail_activity("Xóa bài viết chiến dịch với ID: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        fundraisingCampaignRepo.deleteById(id);
        return "redirect:/dashboard_campaignmanagement";
    }

    @GetMapping("/dashboard_authorization")
    public String render_authorization(Model model) {
        List<Authorization_model> authorizationModels = authorizationRepo.findAll();
        model.addAttribute("authorizationModels",authorizationModels);
        return "page_admin/AuthorizationManagement_admin";
    }

    @GetMapping("/insert/authorizaton")
    public String insertauthorization() {
        return "page_admin/CRUD_AuthorizationManagement/insertAuthorization";
    }

    @PostMapping("/insert/authorization")
    public String insertauthorization(@RequestParam("inputusername") String inputusername,
                                      @RequestParam("inputpassword") String inputpassword,
                                      @RequestParam("confirmpassword") String confirmpassword,
                                      @RequestParam("email") String email,
                                      @RequestParam("inputroles") String inputroles, Model model) {

        // Kiểm tra xem tên tài khoản đã tồn tại
        if (authorizationRepo.existsByUsername(inputusername)) {
            model.addAttribute("error", "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác.");
            return "page_admin/CRUD_AuthorizationManagement/insertAuthorization";
        }

        // Kiểm tra mật khẩu có khớp không
        if (!inputpassword.equals(confirmpassword)) {
            model.addAttribute("error", "Mật khẩu và mật khẩu nhập lại không khớp. Vui lòng thử lại.");
            return "page_admin/CRUD_AuthorizationManagement/insertAuthorization";
        }

        // Tạo và gán dữ liệu vào Authorization_model
        Authorization_model authorizationModel = new Authorization_model();
        authorizationModel.setUsername(inputusername);
        authorizationModel.setPassword(inputpassword); // Có thể mã hóa mật khẩu nếu cần
        authorizationModel.setEmail(email);
        authorizationModel.setRoles(inputroles);

        // Lưu tài khoản mới vào database
        authorizationRepo.save(authorizationModel);


        // Redirect đến dashboard authorization sau khi thành công
        return "redirect:/dashboard_authorization";
    }

    @GetMapping("/authorizaton/{id_update}")
    public String authorizaton_update(@PathVariable("id_update") Long id, Model model) {
        Authorization_model authorizationModel = authorizationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + id));
        model.addAttribute("authorizationModel", authorizationModel);
        return "page_admin/CRUD_AuthorizationManagement/updateAuthorization";
    }

    @PostMapping("/authorizaton/{id_update}")
    public String handle_authorization_update(@RequestParam("inputusername") String inputusername, @RequestParam("inputpassword") String inputpassword, @RequestParam("email") String email,
                                              @RequestParam("inputroles") String inputroles, @PathVariable("id_update") Long id) {
        Authorization_model authorizationModel = authorizationRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        authorizationModel.setUsername(inputusername);
        authorizationModel.setPassword(inputpassword);
        authorizationModel.setEmail(email);
        authorizationModel.setRoles(inputroles);

        authorizationRepo.save(authorizationModel);

        return "redirect:/dashboard_authorization";
    }

    @PostMapping("authorizaton/delete/{id_delete}")
    public String authorizaton_delete(@PathVariable("id_delete") Long id) {
        authorizationRepo.deleteById(id);

        return "redirect:/dashboard_authorization";
    }


    @GetMapping("dashboard_activity")
    public String render_activity(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "startTime", required = false) String startTime,
                                  Model model) {
        List<Activity_model> activityModels;

        // Định dạng lại ngày giờ thành kiểu dễ đọc
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss");

        // Kiểm tra và xử lý tìm kiếm theo ngày giờ
        if (startDate != null && !startDate.isEmpty() && startTime != null && !startTime.isEmpty()) {
            LocalDateTime startDateTime = null;
            String dateTimeStr = startDate + "T" + startTime;  // Ghép ngày và giờ lại thành chuỗi
            try {
                startDateTime = LocalDateTime.parse(dateTimeStr);  // Chuyển thành LocalDateTime
            } catch (DateTimeParseException e) {
                // Nếu xảy ra lỗi khi phân tích, có thể hiển thị thông báo lỗi cho người dùng
                model.addAttribute("error", "Định dạng thời gian không hợp lệ.");
                return "page_admin/Activity_admin"; // Quay lại trang mà không tìm kiếm
            }

            // Tìm kiếm theo thời gian, có thể có hoặc không có username
            if (username != null && !username.isEmpty()) {
                activityModels = activityRepo.findByUsernameAndDatetimeAfter(username, startDateTime);
            } else {
                activityModels = activityRepo.findByDatetimeAfter(startDateTime); // Tìm theo thời gian thôi
            }
        } else {
            // Nếu không có thông tin thời gian, hiển thị tất cả dữ liệu
            activityModels = activityRepo.findAll();
        }

        // Định dạng ngày giờ trước khi truyền vào model để hiển thị
        activityModels.forEach(activity -> activity.setFormattedDatetime(activity.getDatetime().format(formatter)));

        model.addAttribute("activityModels", activityModels);
        model.addAttribute("username", username);
        model.addAttribute("startDate", startDate);
        model.addAttribute("startTime", startTime);
        return "page_admin/Activity_admin";
    }






    @GetMapping("/email")
    public String showEmailForm(Model model) {
        return "page_admin/Handle/FormEmail";
    }

    @PostMapping("/send-email")
    public String sendEmail(String to, String subject, String message, Model model) {
        emailService.sendMail(to, subject, message);
        model.addAttribute("success", "Email đã được gửi thành công!");
        return "page_admin/Handle/ResponeEmail";
    }
}
