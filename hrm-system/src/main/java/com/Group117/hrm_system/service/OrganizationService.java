package com.Group117.hrm_system.service;

import com.Group117.hrm_system.Repository.*;
import com.Group117.hrm_system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired private ChiNhanhRepository chiNhanhRepository;
    @Autowired private PhongBanRepository phongBanRepository;
    @Autowired private NhomRepository nhomRepository;
    @Autowired private ChucVuRepository chucVuRepository;

    // --- QUẢN LÝ CHI NHÁNH (Branch) ---
    public List<ChiNhanh> getAllChiNhanh() { return chiNhanhRepository.findAll(); }
    public ChiNhanh createChiNhanh(ChiNhanh cn) {
        if (cn.getId() == null || cn.getId().isEmpty()) cn.setId("CN-" + generateId());
        return chiNhanhRepository.save(cn);
    }
    public void deleteChiNhanh(String id) { chiNhanhRepository.deleteById(id); }

    // --- QUẢN LÝ PHÒNG BAN (Department) ---
    public List<PhongBan> getAllPhongBan() { return phongBanRepository.findAll(); }
    public PhongBan createPhongBan(PhongBan pb) {
        if (pb.getId() == null || pb.getId().isEmpty()) pb.setId("PB-" + generateId());
        return phongBanRepository.save(pb);
    }
    public void deletePhongBan(String id) { phongBanRepository.deleteById(id); }

    // --- QUẢN LÝ NHÓM (Group) ---
    public List<Nhom> getAllNhom() { return nhomRepository.findAll(); }
    public Nhom createNhom(Nhom nhom) {
        if (nhom.getId() == null || nhom.getId().isEmpty()) nhom.setId("NH-" + generateId());
        return nhomRepository.save(nhom);
    }
    public void deleteNhom(String id) { nhomRepository.deleteById(id); }

    // --- QUẢN LÝ CHỨC VỤ (Đã làm) ---
    public List<ChucVu> getAllChucVu() { return chucVuRepository.findAll(); }
    public ChucVu createChucVu(ChucVu cv) {
        if (cv.getId() == null || cv.getId().isEmpty()) cv.setId("CV-" + generateId());
        return chucVuRepository.save(cv);
    }

    // Hàm bổ trợ tạo mã ngẫu nhiên 4 ký tự
    private String generateId() {
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}