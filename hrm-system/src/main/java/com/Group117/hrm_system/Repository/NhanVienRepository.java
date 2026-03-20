package com.Group117.hrm_system.Repository;

import com.Group117.hrm_system.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    // Tìm nhân viên dựa vào email công việc
    NhanVien findByEmailCongViec(String email);
}