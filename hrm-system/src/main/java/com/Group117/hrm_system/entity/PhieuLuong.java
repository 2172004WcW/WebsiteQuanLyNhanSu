package com.Group117.hrm_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "phieu_luong")
@Data
public class PhieuLuong {
    @Id
    @Column(name = "id")
    private String id; // VD: PL_NV001_032026

    @Column(name = "thang_nam")
    private String thangNam;

    @Column(name = "phat_muon")
    private Double phatMuon;

    @Column(name = "nghi_khong_phep")
    private Double nghiKhongPhep;

    @Column(name = "tong_luong")
    private Double tongLuong;

    @Column(name = "trang_thai_thanh_toan")
    private String trangThaiThanhToan; // "CHUA_THANH_TOAN", "DA_THANH_TOAN"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhan_vien_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private NhanVien nhanVien;
}