package com.Group117.hrm_system.Controller;

import com.Group117.hrm_system.entity.BangLuong;
import com.Group117.hrm_system.entity.PhieuLuong;
import com.Group117.hrm_system.entity.TaiKhoan;
import com.Group117.hrm_system.Repository.BangLuongRepository;
import com.Group117.hrm_system.Repository.TaiKhoanRepository;
import com.Group117.hrm_system.Repository.PhieuLuongRepository;
import com.Group117.hrm_system.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PhieuLuongRepository phieuLuongRepository;

    @Autowired
    private BangLuongRepository bangLuongRepository;

    @Autowired
    private PayrollService payrollService;

    // 1. API dành cho Nhân viên: Xem danh sách phiếu lương của chính mình
    @GetMapping("/my-payslips")
    public ResponseEntity<?> getMyPayslips() {
        // Lấy username từ SecurityContext (tương tự ProfileController)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TaiKhoan tk = taiKhoanRepository.findByUsername(username);

        if (tk == null || tk.getNhanVien() == null) {
            return ResponseEntity.status(404).body("Không tìm thấy thông tin nhân viên");
        }

        // Lấy danh sách phiếu lương theo NhanVienId (dựa trên Repository đã viết)
        List<PhieuLuong> list = phieuLuongRepository.findByNhanVienId(tk.getNhanVien().getId());
        return ResponseEntity.ok(list);
    }

    // 2. API dành cho Admin: Thiết lập cấu hình lương theo chức vụ
    @PostMapping("/structure")
    public ResponseEntity<?> setupSalaryStructure(@RequestBody BangLuong bangLuong) {
        BangLuong saved = bangLuongRepository.save(bangLuong);
        return ResponseEntity.ok(saved);
    }

    // 3. API dành cho Admin: Kích hoạt quét dữ liệu và tính lương cuối tháng
    @PostMapping("/generate")
    public ResponseEntity<?> generateMonthlyPayroll(@RequestParam String thangNam) {
        try {
            payrollService.scanAndCalculatePayroll(thangNam);
            return ResponseEntity.ok("Tổng hợp lương tháng " + thangNam + " thành công");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    // 4. API dành cho Kế toán: Cập nhật trạng thái thanh toán phiếu lương
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String id, @RequestParam String status) {
        return phieuLuongRepository.findById(id)
                .map(pl -> {
                    pl.setTrangThaiThanhToan(status);
                    phieuLuongRepository.save(pl);
                    return ResponseEntity.ok("Cập nhật trạng thái thành công");
                })
                .orElse(ResponseEntity.status(404).body("Không tìm thấy phiếu lương"));
    }
}