package com.Group117.hrm_system.Controller;

import com.Group117.hrm_system.entity.NhanVien;
import com.Group117.hrm_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 1. Lấy danh sách nhân viên (Dùng cho bảng danh sách nhân viên)
    @GetMapping
    public ResponseEntity<List<NhanVien>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // 2. Thêm mới nhân viên (Onboarding) - Chỉ Admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/onboard")
    public ResponseEntity<NhanVien> onboardEmployee(@RequestBody NhanVien nhanVien) {
        return ResponseEntity.ok(employeeService.onboardEmployee(nhanVien));
    }

    // 3. Cập nhật hồ sơ cá nhân (CCCD, SĐT, Email...)
    @PutMapping("/{id}/update-profile")
    public ResponseEntity<NhanVien> updateProfile(
            @PathVariable String id,
            @RequestBody NhanVien updatedInfo) {
        // Gọi thẳng vào Service để xử lý, không gọi trực tiếp Repository ở đây
        return ResponseEntity.ok(employeeService.updateProfile(id, updatedInfo));
    }
    // Lấy chi tiết 1 nhân viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<NhanVien> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}