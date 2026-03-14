package com.Group117.hrm_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "taikhoan")
@Data
public class TaiKhoan {

    @Id
    @Column(name = "ma_tai_khoan") // Nếu ông dùng ten_dang_nhap làm PK thì đổi tên field này
    private String maTaiKhoan;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    @Column(name = "trang_thai_tai_khoan")
    private boolean trangThaiTaiKhoan = true; // Mặc định là true (đang hoạt động)

    @OneToOne
    @JoinColumn(name = "id_nhan_vien", referencedColumnName = "id")
    @ToString.Exclude
    private NhanVien nhanVien;
}