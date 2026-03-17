package com.Group117.hrm_system.Controller;

import com.Group117.hrm_system.config.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    @Autowired
    private JwtService jwtService;
    // GET /dashboard/director → templates/dashboard/director.html
    @GetMapping("/dashboard/director")
    public String directorPage(HttpServletRequest request, Model model) {

        // 1. Lấy token từ cookie (browser tự gửi sau khi login)
        String token = getTokenFromCookie(request);

        if (token == null) {
            System.out.println(">>> [Director] Không có token → redirect login");
            return "redirect:/login?error=unauthorized";
        }

        try {
            // 2. Kiểm tra role từ JWT claim
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [Director] Role từ token: " + role);

            if (!"DIRECTOR".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                System.out.println(">>> [Director] Không đủ quyền, role: " + role);
                return "redirect:/login?error=forbidden";
            }

            // 3. Truyền data vào Thymeleaf model
            String username = jwtService.extractUsername(token);
            LocalDate now   = LocalDate.now();

            model.addAttribute("username",     username);
            model.addAttribute("currentMonth", now.getMonthValue());
            model.addAttribute("currentYear",  now.getYear());
            model.addAttribute("role",         role);

            System.out.println(">>> [Director] Render dashboard cho: " + username);
            return "dashboard/director";

        } catch (Exception e) {
            System.err.println(">>> [Director] Lỗi token: " + e.getMessage());
            return "redirect:/login?error=invalid_token";
        }
    }

    // Helper: đọc JWT từ cookie "jwt_token"
    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if ("jwt_token".equals(c.getName())) {
                String value = c.getValue();
                if (value != null
                        && !value.isEmpty()
                        && !value.equalsIgnoreCase("null")
                        && !value.equalsIgnoreCase("undefined")) {
                    return value;
                }
            }
        }
        return null;
    }
}