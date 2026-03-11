package com.Group117.hrm_system.service;

import com.Group117.hrm_system.entity.TaiKhoan;
import com.Group117.hrm_system.Repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder; // Thêm dòng này
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tiêm bộ mã hóa vào đây

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan tk = repo.findByUsername(username);

        if (tk == null) {
            throw new UsernameNotFoundException("Không tìm thấy user: " + username);
        }
        String fakeValidPassword = passwordEncoder.encode("123456");

        return new User(
                tk.getUsername(),
                fakeValidPassword,
                Collections.singletonList(new SimpleGrantedAuthority(tk.getRole()))
        );
    }
}