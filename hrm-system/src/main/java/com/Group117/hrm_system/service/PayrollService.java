package com.Group117.hrm_system.service;

import com.Group117.hrm_system.Repository.BangLuongRepository;
import com.Group117.hrm_system.Repository.NhanVienRepository;
import com.Group117.hrm_system.Repository.PhieuLuongRepository;
import com.Group117.hrm_system.entity.BangLuong;
import com.Group117.hrm_system.entity.NhanVien;
import com.Group117.hrm_system.entity.PhieuLuong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PayrollService {

    @Autowired
    private PhieuLuongRepository phieuLuongRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private BangLuongRepository bangLuongRepository;

    // 1. Quét dữ liệu và tổng hợp lương cuối tháng cho toàn bộ nhân viên
    public void scanAndCalculatePayroll(String thangNam) {
        List<NhanVien> allEmployees = nhanVienRepository.findAll();

        for (NhanVien nv : allEmployees) {
            // Kiểm tra xem nhân viên đã có cấu hình lương (BangLuong) chưa
            if (nv.getBangLuong() == null) {
                continue; // Bỏ qua nếu nhân viên chưa được thiết lập lương cơ bản
            }

            // Kiểm tra xem tháng này đã tạo phiếu lương chưa để tránh trùng lặp
            List<PhieuLuong> existing = phieuLuongRepository.findByNhanVienIdAndThangNam(nv.getId(), thangNam);
            if (!existing.isEmpty()) {
                continue; 
            }

            // Giả lập lấy dữ liệu từ hệ thống chấm công và nghỉ phép
            // Trong thực tế, bạn sẽ gọi AttendanceService hoặc LeaveService tại đây
            double soPhutMuon = 30.0; // VD: Muộn 30 phút
            double soNgayNghiKhongPhep = 1.0; // VD: Nghỉ 1 ngày không phép

            // Logic Tính lương: Tổng = (Lương CB + Phụ cấp) - (Phạt muộn) - (Nghỉ không phép)
            double luongCB = nv.getBangLuong().getLuongCoBan();
            double phuCap = nv.getBangLuong().getPhuCapDinhMuc();
            
            // Định mức phạt giả định (có thể đưa vào cấu hình hệ thống)
            double tienPhatMuon = soPhutMuon * 2000; // 2k/phút
            double tienPhatNghi = soNgayNghiKhongPhep * 300000; // 300k/ngày

            double tongLuong = (luongCB + phuCap) - (tienPhatMuon) - (tienPhatNghi);

            // Tạo thực thể PhieuLuong mới
            PhieuLuong phieuLuong = new PhieuLuong();
            phieuLuong.setId("PL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            phieuLuong.setNhanVien(nv);
            phieuLuong.setThangNam(thangNam);
            phieuLuong.setPhatMuon(tienPhatMuon);
            phieuLuong.setNghiKhongPhep(tienPhatNghi);
            phieuLuong.setTongLuong(tongLuong);
            phieuLuong.setTrangThaiThanhToan("CHUA_THANH_TOAN");

            phieuLuongRepository.save(phieuLuong);
        }
    }

    // 2. Cập nhật trạng thái thanh toán (Payslip Management)
    public PhieuLuong updatePaymentStatus(String id, String status) {
        PhieuLuong existingPhieu = phieuLuongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu lương với mã: " + id));

        existingPhieu.setTrangThaiThanhToan(status);
        return phieuLuongRepository.save(existingPhieu);
    }

    // 3. Lấy phiếu lương theo ID
    public PhieuLuong getPayrollById(String id) {
        return phieuLuongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu lương: " + id));
    }

    // 4. Lấy danh sách phiếu lương của một nhân viên
    public List<PhieuLuong> getPayrollByEmployee(String employeeId) {
        return phieuLuongRepository.findByNhanVienId(employeeId);
    }
}
