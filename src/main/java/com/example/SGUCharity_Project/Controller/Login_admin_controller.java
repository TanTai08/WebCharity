package com.example.SGUCharity_Project.Controller;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.SGUCharity_Project.Model.Account_admin_model;
import com.example.SGUCharity_Project.Repository.Account_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Controller
public class Login_admin_controller<UserDetails> {

    @GetMapping("/login")
    public String logout() {
        return "account_admin/SignIn";
    }

    @Autowired
    private Account_Repo userRepository;

    // Phương thức Service - Load user details for authentication
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Tìm người dùng dựa trên tên đăng nhập
//        Account_admin_model account = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Trả về một đối tượng UserDetails chứa thông tin người dùng
//        return new org.springframework.security.core.userdetails.User(
//                account.getUsername(),
//                account.getPassword(),
//                mapRolesToAuthorities(account.getRoles())
//        );
//    }

    // Phương thức Controller - Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        // Tìm người dùng dựa trên username
        Optional<Account_admin_model> userOptional = userRepository.findByUsername(username);

        // Kiểm tra xem người dùng có tồn tại và mật khẩu có khớp không
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            // Nếu khớp, chuyển hướng tới trang dashboard (hoặc trang khác bạn muốn)
            return "redirect:/dashboard";
        }

        // Nếu không khớp, trả về trang login và báo lỗi
        model.addAttribute("error", "Invalid username or password.");
        return "login"; // Trả về trang login
    }

    // Phương thức Controller - Xử lý hiển thị trang quản lý tài khoản
    @GetMapping("/accountManagement")
    public String showAccountManagementPage(Model model) {
        // Lấy danh sách tất cả người dùng
        List<Account_admin_model> accounts = userRepository.findAll();

        // Thêm danh sách người dùng vào model để hiển thị trong view
        model.addAttribute("accounts", accounts);

        return "page_admin/accountManagement"; // Trả về trang AccountManagement.html
    }

}
