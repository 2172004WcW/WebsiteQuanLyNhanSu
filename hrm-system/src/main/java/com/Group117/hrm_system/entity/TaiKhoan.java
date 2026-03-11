package com.Group117.hrm_system.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "taikhoan")
@Data
public class TaiKhoan {

    @Id
    @Column(name = "ma_tai_khoan") // Chỉ định rõ tên cột trong SQL
    private String maTaiKhoan;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}