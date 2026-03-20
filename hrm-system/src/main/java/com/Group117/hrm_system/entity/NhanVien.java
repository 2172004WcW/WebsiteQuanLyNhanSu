package com.Group117.hrm_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Entity
@Table(name = "nhan_vien")
@Data
@SQLDelete(sql = "UPDATE nhan_vien SET trang_thai_hoat_dong = 'DA_NGHI_VIEC' WHERE id = ?")
@SQLRestriction("trang_thai_hoat_dong != 'DA_NGHI_VIEC'")
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

    // Trạng thái cho Soft Delete (DANG_LAM_VIEC, DA_NGHI_VIEC)
    @Column(name = "trang_thai_hoat_dong")
    private String trangThaiHoatDong = "DANG_LAM_VIEC";

    @Column(name = "he_so_luong")
    private Float heSoLuong;

    // --- MAPPING VỚI TỔ CHỨC ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phong_ban_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PhongBan phongBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhom_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Nhom nhom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuc_vu_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ChucVu chucVu;

    // --- MAPPING SELF-REFERENCE ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_quan_ly_truoc_tiep_id")
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private NhanVien nguoiQuanLyTruocTiep;

    // --- MAPPING VỚI TÀI KHOẢN ---
    @OneToOne(mappedBy = "nhanVien")
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ToString.Exclude
    private TaiKhoan taiKhoan;
}