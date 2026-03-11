package com.Group117.hrm_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF để tránh lỗi chặn Form POST
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình phân quyền
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 3. Cấu hình Form Login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // 4. Cấu hình Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }
}