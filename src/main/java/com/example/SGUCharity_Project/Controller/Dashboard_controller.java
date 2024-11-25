package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.*;
import com.example.SGUCharity_Project.Repository.*;

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
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
import java.util.Collections;
>>>>>>> 14e683caedc71ed8c502992a9e3fc329f0fe87c4
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

<<<<<<< HEAD
    @Autowired
    Activity_Repo activityRepo;

    // Render ra trang dashboard
=======

    // Render ra trang dashboar
>>>>>>> 14e683caedc71ed8c502992a9e3fc329f0fe87c4
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
            @RequestParam("goalAmount") double goalAmount,
            @RequestParam("content1") String content1,
            @RequestParam("content2") String content2,
            @RequestParam("content3") String content3,
            @RequestParam("imgContent") String imgContent,
<<<<<<< HEAD
            @RequestParam("imgContent2") String imgContent2,HttpSession session) {
=======
            @RequestParam("imgContent2") String imgContent2,
            @RequestParam("code") String code) {
>>>>>>> 14e683caedc71ed8c502992a9e3fc329f0fe87c4

        Artical_model artical = new Artical_model();
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStartDate(LocalDate.parse(startDate));
        artical.setEndDate(LocalDate.parse(endDate));
        artical.setGoalAmount(goalAmount);
        artical.setAmountRaised(0); // Initialize với 0
        artical.setCode(code);
        artical.setStatus("Active");
        artical.setDisplaycategory("Default");

        Artical_model savedArtical = charitycontentRepo.save(artical);

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
        activityModel.setUsername(username);
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm một chương trình mới");
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_programmanagement";
    }



    @GetMapping("programmanagement/{id_update}")
    public String programmanagement_update(@PathVariable("id_update") Long id, Model model) {
        Artical_model articalModel = charitycontentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        Articaldetail_model articaldetailModels = articalDetailRepo.findFirstByArtical_Id(id);
        model.addAttribute("articalModel", articalModel);
        model.addAttribute("articaldetailModels", articaldetailModels);

        return "page_admin/CRUD_ProgramManagement/updateProgram";
    }



    @PostMapping("programmanagement/{id_update}")
    public String handle_programmanagement_update(
            @PathVariable("id_update") Long id,
            @RequestParam("img") String inputimg,  // Match form field name
            @RequestParam("title") String inputtitle,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("goalAmount") double goalAmount,
            @RequestParam("content1") String content1,
            @RequestParam("content2") String content2,
            @RequestParam("content3") String content3,
            @RequestParam("imgContent") String imgContent,
            @RequestParam("imgContent2") String imgContent2,
<<<<<<< HEAD
            HttpSession session) {
=======
            @RequestParam("code") String code) {
>>>>>>> 14e683caedc71ed8c502992a9e3fc329f0fe87c4

        Artical_model artical = charitycontentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + id));

        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStartDate(LocalDate.parse(startDate));
        artical.setEndDate(LocalDate.parse(endDate));
        artical.setGoalAmount(goalAmount);
        artical.setCode(code);

        charitycontentRepo.save(artical);

        Articaldetail_model articalDetail = articalDetailRepo.findFirstByArtical_Id(id);
        articalDetail.setContent_1(content1);
        articalDetail.setContent_2(content2);
        articalDetail.setContent_3(content3);
        articalDetail.setImg_content(imgContent);
        articalDetail.setImg_content2(imgContent2);

        articalDetailRepo.save(articalDetail);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Sửa");
        activityModel.setDetail_activity("Sửa bài viết chương trình với ID: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
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
<<<<<<< HEAD
                                 @RequestParam("detailUrl") String detailUrl,
                                 HttpSession session) {
=======
                                 @RequestParam("goalAmount") String code,
                                 @RequestParam("detailUrl") String detailUrl) {
>>>>>>> 14e683caedc71ed8c502992a9e3fc329f0fe87c4

        // Tạo một đối tượng mới và thiết lập các thuộc tính
        FundraisingCampaign_model fundraisingCampaignModel = new FundraisingCampaign_model();
        fundraisingCampaignModel.setTitle(title);
        fundraisingCampaignModel.setImgUrl(imgUrl);
        fundraisingCampaignModel.setStartDate(LocalDate.parse(startDate));
        fundraisingCampaignModel.setEndDate(LocalDate.parse(endDate));
        fundraisingCampaignModel.setAmountRaised(0);
        fundraisingCampaignModel.setGoalAmount(Double.parseDouble(goalAmount));
        fundraisingCampaignModel.setCode(code);
        fundraisingCampaignModel.setStatus("Đang vận động");
        fundraisingCampaignModel.setDetailUrl(detailUrl);
        fundraisingCampaignModel.setCategory("Bệnh hiểm nghèo");

        // Lưu vào database mà không thay đổi ID
        fundraisingCampaignRepo.save(fundraisingCampaignModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Thêm");
        activityModel.setDetail_activity("Thêm một chiến dịch mới");
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_campaignmanagement";
    }

    @GetMapping("campaignmanagement/{id_update}")
    public String campaignmanagement_update(@PathVariable("id_update") Long id, Model model) {
        FundraisingCampaign_model fundraisingCampaignModel = fundraisingCampaignRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID " + id));
        model.addAttribute("fundraisingCampaignModel", fundraisingCampaignModel);
        return "page_admin/CRUD_CampaignManagement/updateCampaign";
    }

    @PostMapping("campaignmanagement/{id_update}")
    public String handle_campaignmanagement_update(@RequestParam("title") String title,
                                                   @RequestParam("imgUrl") String imgUrl,
                                                   @RequestParam("startDate") String startDate, // Correct name for startDate
                                                   @RequestParam("endDate") String endDate, // Correct name for endDate
                                                   @RequestParam("goalAmount") String goalAmount,
                                                   @RequestParam("code") String code,
                                                   @RequestParam("detailUrl") String detailUrl, // detailUrl is a request parameter, not a path variable
                                                   @PathVariable("id_update") Long id,
                                                   HttpSession session) {
        FundraisingCampaign_model fundraisingCampaignModel = fundraisingCampaignRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID " + id));

        // Update fields
        fundraisingCampaignModel.setTitle(title);
        fundraisingCampaignModel.setImgUrl(imgUrl);
        fundraisingCampaignModel.setStartDate(LocalDate.parse(startDate)); // Set startDate
        fundraisingCampaignModel.setEndDate(LocalDate.parse(endDate)); // Set endDate
        fundraisingCampaignModel.setGoalAmount(Double.parseDouble(goalAmount));
        fundraisingCampaignModel.setDetailUrl(detailUrl);
        fundraisingCampaignModel.setCode(code);

        // Save updated campaign
        fundraisingCampaignRepo.save(fundraisingCampaignModel);

        // Lấy username từ session
        String username = (String) session.getAttribute("username");
        // Tạo log cho hoạt động
        Activity_model activityModel = new Activity_model();
        activityModel.setUsername(username);
        activityModel.setActivity("Sửa");
        activityModel.setDetail_activity("Xóa bài viết chiến dịch với ID: " + id);
        activityModel.setDatetime(LocalDateTime.now());
        // Lưu log hoạt động
        activityRepo.save(activityModel);

        return "redirect:/dashboard_campaignmanagement";
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
                                      @RequestParam("email") String email,
                                      @RequestParam("inputroles") String inputroles) {

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
    public String render_activity(Model model) {
        List<Activity_model> activityModels = activityRepo.findAll();
        model.addAttribute("activityModels",activityModels);
        return "page_admin/Activity_admin";
    }
}
