package com.Group117.hrm_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.Date;

@Entity
@Table(name = "nhan_vien")
@Data
public class NhanVien {
    @Id
    private String id; // primary key

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @Column(name = "so_cccd", unique = true)
    private String soCccd;

    @Column(name = "ngay_cap")
    private Date ngayCap;

    @Column(name = "noi_cap")
    private String noiCap;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "email_cong_viec", unique = true)
    private String emailCongViec;

    @Column(name = "dia_chi_tam_tru", columnDefinition = "TEXT")
    private String diaChiTamTru;

    @Column(name = "anh_dai_dien_url")
    private String anhDaiDienUrl;

    @Column(name = "ngay_vao_lam")
    private Date ngayVaoLam;

    @Column(name = "trang_thai_hoat_dong")
    private String trangThaiHoatDong;

    @Column(name = "phong_ban_id")
    private String phongBanId;

    @Column(name = "nhom_id")
    private String nhomId;

    @Column(name = "chuc_vu_id")
    private String chucVuId;

    @Column(name = "nguoi_quan_ly_truoc_tiep_id")
    private String nguoiQuanLyTruocTiepId;

    @Column(name = "he_so_luong")
    private Float heSoLuong;

    @OneToOne(mappedBy = "nhanVien")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private TaiKhoan taiKhoan;
}