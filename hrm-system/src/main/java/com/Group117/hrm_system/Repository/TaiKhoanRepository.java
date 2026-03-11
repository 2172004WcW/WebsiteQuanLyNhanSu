package com.Group117.hrm_system.Repository;
import com.Group117.hrm_system.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository; // Cần thêm dòng này
import org.springframework.stereotype.Repository;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    TaiKhoan findByUsername(String username);
}