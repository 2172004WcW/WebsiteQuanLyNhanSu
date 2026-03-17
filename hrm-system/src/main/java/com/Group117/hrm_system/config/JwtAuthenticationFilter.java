package com.Group117.hrm_system.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Bỏ qua các path public — không cần xử lý JWT
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/")
                || path.equals("/login")
                || path.equals("/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất JWT — ưu tiên Authorization header, fallback sang Cookie
        String jwt = extractToken(request);

        // 3. Không có token hợp lệ → cho đi tiếp (Spring Security sẽ xử lý sau)
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Xác thực token và set SecurityContext
        try {
            String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Lưu vào SecurityContext — từ đây request được coi là đã xác thực
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println(">>> JWT hợp lệ, đã xác thực user: " + username);
                }
            }
        } catch (Exception e) {
            // Token lỗi (hết hạn, sai chữ ký...) → log và bỏ qua, không crash request
            System.err.println(">>> Lỗi xác thực JWT: " + e.getMessage());
        }

        // 5. Luôn cho filterChain đi tiếp
        filterChain.doFilter(request, response);
    }

    /**
     * Trích xuất JWT từ request theo thứ tự ưu tiên:
     * 1. Authorization: Bearer <token>  (dùng cho Fetch/Axios API calls)
     * 2. Cookie jwt_token               (dùng cho Thymeleaf page navigation)
     *
     * @return JWT string hợp lệ, hoặc null nếu không tìm thấy
     */
    private String extractToken(HttpServletRequest request) {
        // Ưu tiên 1: Authorization header (REST API calls từ JS)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            if (isValidTokenString(token)) {
                return token;
            }
        }

        // Ưu tiên 2: Cookie (Thymeleaf page navigation — browser tự gửi)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (isValidTokenString(token)) {
                        return token;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Kiểm tra chuỗi token không phải rác từ frontend
     */
    private boolean isValidTokenString(String token) {
        return token != null
                && !token.isEmpty()
                && !token.equalsIgnoreCase("null")
                && !token.equalsIgnoreCase("undefined");
    }
}