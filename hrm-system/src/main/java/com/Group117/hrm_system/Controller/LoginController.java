package com.Group117.hrm_system.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trả về login.html
    }

    @GetMapping("/home")
    public String showHomePage() {
        // Lưu ý: Với JWT, bạn sẽ dùng JavaScript ở trang này
        // để gửi Token lên mỗi khi load dữ liệu.
        return "index";
    }
}