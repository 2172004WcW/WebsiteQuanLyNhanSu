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

    // ─────────────────────────────────────────────────────────
    // GET /dashboard/director → templates/dashboard/director.html
    // Role được phép : DIRECTOR
    // Quyền hạn      : Xem báo cáo chiến lược, read-only
    // ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard/director")
    public String directorPage(HttpServletRequest request, Model model) {

        String token = getTokenFromCookie(request);
        if (token == null) {
            System.out.println(">>> [Director] Không có token → redirect login");
            return "redirect:/login?error=unauthorized";
        }

        try {
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [Director] Role từ token: " + role);

            if (!"DIRECTOR".equalsIgnoreCase(role)) {
                System.out.println(">>> [Director] Không đủ quyền, role: " + role);
                return "redirect:/login?error=forbidden";
            }

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

    // ─────────────────────────────────────────────────────────
    // GET /dashboard/admin → templates/dashboard/admin.html
    // Role được phép : ADMIN
    // Quyền hạn      : Quản trị tài khoản, phân quyền, cấu hình hệ thống
    //                  KHÔNG can thiệp vào nghiệp vụ nhân sự
    // ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard/admin")
    public String adminPage(HttpServletRequest request, Model model) {

        String token = getTokenFromCookie(request);
        if (token == null) {
            System.out.println(">>> [Admin] Không có token → redirect login");
            return "redirect:/login?error=unauthorized";
        }

        try {
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [Admin] Role từ token: " + role);

            if (!"ADMIN".equalsIgnoreCase(role)) {
                System.out.println(">>> [Admin] Không đủ quyền, role: " + role);
                return "redirect:/login?error=forbidden";
            }

            String username = jwtService.extractUsername(token);
            LocalDate now   = LocalDate.now();

            model.addAttribute("username",     username);
            model.addAttribute("currentMonth", now.getMonthValue());
            model.addAttribute("currentYear",  now.getYear());
            model.addAttribute("role",         role);

            System.out.println(">>> [Admin] Render dashboard cho: " + username);
            return "dashboard/admin";

        } catch (Exception e) {
            System.err.println(">>> [Admin] Lỗi token: " + e.getMessage());
            return "redirect:/login?error=invalid_token";
        }
    }

    // ─────────────────────────────────────────────────────────
    // GET /dashboard/hr → templates/dashboard/hr.html
    // Role được phép : HR
    // Quyền hạn      : Toàn bộ nghiệp vụ nhân sự — lương, phép,
    //                  tuyển dụng, phòng ban, chấm công
    //                  Xem dữ liệu mọi phòng ban, không giới hạn departmentId
    // ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard/hr")
    public String hrPage(HttpServletRequest request, Model model) {

        String token = getTokenFromCookie(request);
        if (token == null) {
            System.out.println(">>> [HR] Không có token → redirect login");
            return "redirect:/login?error=unauthorized";
        }

        try {
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [HR] Role từ token: " + role);

            if (!"HR".equalsIgnoreCase(role)) {
                System.out.println(">>> [HR] Không đủ quyền, role: " + role);
                return "redirect:/login?error=forbidden";
            }

            String username = jwtService.extractUsername(token);
            LocalDate now   = LocalDate.now();

            model.addAttribute("username",     username);
            model.addAttribute("currentMonth", now.getMonthValue());
            model.addAttribute("currentYear",  now.getYear());
            model.addAttribute("role",         role);

            System.out.println(">>> [HR] Render dashboard cho: " + username);
            return "dashboard/hr";

        } catch (Exception e) {
            System.err.println(">>> [HR] Lỗi token: " + e.getMessage());
            return "redirect:/login?error=invalid_token";
        }
    }

    // ─────────────────────────────────────────────────────────
    // GET /dashboard/employee → templates/dashboard/employee.html
    // Role được phép : EMPLOYEE
    // Quyền hạn      : Tự phục vụ cá nhân, xem thông tin của chính mình
    // ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard/employee")
    public String employeePage(HttpServletRequest request, Model model) {

        String token = getTokenFromCookie(request);
        if (token == null) {
            System.out.println(">>> [Employee] Không có token → redirect login");
            return "redirect:/login?error=unauthorized";
        }

        try {
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [Employee] Role từ token: " + role);

            if (!"EMPLOYEE".equalsIgnoreCase(role)) {
                System.out.println(">>> [Employee] Không đủ quyền, role: " + role);
                return "redirect:/login?error=forbidden";
            }

            String username = jwtService.extractUsername(token);
            LocalDate now   = LocalDate.now();

            model.addAttribute("username",     username);
            model.addAttribute("currentMonth", now.getMonthValue());
            model.addAttribute("currentYear",  now.getYear());
            model.addAttribute("role",         role);

            System.out.println(">>> [Employee] Render dashboard cho: " + username);
            return "dashboard/employee";

        } catch (Exception e) {
            System.err.println(">>> [Employee] Lỗi token: " + e.getMessage());
            return "redirect:/login?error=invalid_token";
        }
    }

    // ─────────────────────────────────────────────────────────
    // GET /dashboard → Router tự động redirect theo role trong JWT
    // Gọi từ LoginController sau khi xác thực thành công
    //
    // Thứ tự cấp bậc trong hệ thống HRM:
    //   DIRECTOR  → /dashboard/director  (cao nhất, read-only báo cáo)
    //   ADMIN     → /dashboard/admin     (quản trị kỹ thuật, tài khoản)
    //   HR        → /dashboard/hr        (nghiệp vụ nhân sự toàn hệ thống)
    //   EMPLOYEE  → /dashboard/employee  (thấp nhất, self-service cá nhân)
    // ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboardRouter(HttpServletRequest request) {

        String token = getTokenFromCookie(request);
        if (token == null) {
            return "redirect:/login?error=unauthorized";
        }

        try {
            String role = jwtService.extractClaim(token,
                    claims -> claims.get("role", String.class));

            System.out.println(">>> [Router] Role: " + role + " → redirect dashboard tương ứng");

            return switch (role.toUpperCase()) {
                case "DIRECTOR" -> "redirect:/dashboard/director";
                case "ADMIN"    -> "redirect:/dashboard/admin";
                case "HR"       -> "redirect:/dashboard/hr";
                case "EMPLOYEE" -> "redirect:/dashboard/employee";
                default -> {
                    System.out.println(">>> [Router] Role không hợp lệ: " + role);
                    yield "redirect:/login?error=forbidden";
                }
            };

        } catch (Exception e) {
            System.err.println(">>> [Router] Lỗi token: " + e.getMessage());
            return "redirect:/login?error=invalid_token";
        }
    }

    // ─────────────────────────────────────────────────────────
    // Helper: đọc JWT từ cookie "jwt_token"
    // ─────────────────────────────────────────────────────────
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