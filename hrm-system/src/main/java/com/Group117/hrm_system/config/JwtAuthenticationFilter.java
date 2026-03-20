package com.Group117.hrm_system.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy đường dẫn request
        String path = request.getServletPath();

        // 2. Nếu là đường dẫn đăng nhập hoặc file tĩnh, cho đi qua luôn (né lỗi 403)
        if (path.startsWith("/api/auth/") || path.equals("/login") || path.equals("/")
                || path.startsWith("/css/") || path.startsWith("/js/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Kiểm tra Header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Nếu không có Header hoặc không đúng chuẩn Bearer thì cho đi tiếp (vào Filter tiếp theo)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 4. Trích xuất và kiểm tra Token
            jwt = authHeader.substring(7);

            // Chặn các giá trị rác thường gặp từ Frontend như chuỗi "null", "undefined" hoặc rỗng
            if (jwt.isEmpty() || jwt.equalsIgnoreCase("null") || jwt.equalsIgnoreCase("undefined")) {
                filterChain.doFilter(request, response);
                return;
            }

            username = jwtService.extractUsername(jwt);

            // 5. Xác thực User nếu chưa được xác thực trong SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Kiểm tra tính hợp lệ của token (hạn dùng, chữ ký)
                if (jwtService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Lưu thông tin xác thực vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log lỗi nhẹ nhàng ra console để dễ debug mà không làm sập luồng request
            System.err.println(">>> Lỗi xác thực JWT: " + e.getMessage());
        }

        // 6. Luôn cho phép filterChain đi tiếp
        filterChain.doFilter(request, response);
    }
}