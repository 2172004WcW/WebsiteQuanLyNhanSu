package com.Group117.hrm_system.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trả về file login.html
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "index"; // Bạn nên có 1 file index.html để sau khi login thành công sẽ vào đây
    }
}