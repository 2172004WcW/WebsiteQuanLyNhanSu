package com.Group117.hrm_system.Controller;

import com.Group117.hrm_system.entity.YeuCauTuyenDung;
import com.Group117.hrm_system.entity.HoSoUngVien;
import com.Group117.hrm_system.entity.LichPhongVan;
import com.Group117.hrm_system.entity.NhanVien;
import com.Group117.hrm_system.Repository.YeuCauTuyenDungRepository;
import com.Group117.hrm_system.Repository.HoSoUngVienRepository;
import com.Group117.hrm_system.Repository.LichPhongVanRepository;
import com.Group117.hrm_system.Repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
@CrossOrigin(origins = "*")
public class RecruitmentController {

    @Autowired
    private YeuCauTuyenDungRepository yeuCauRepo;

    @Autowired
    private HoSoUngVienRepository candidateRepo;

    @Autowired
    private LichPhongVanRepository interviewRepo;

    @Autowired
    private NhanVienRepository nhanVienRepo;

    // --- GIỮ NGUYÊN CÁC API CŨ (1-6) ---

    @PostMapping("/hiring-request")
    public ResponseEntity<?> createHiringRequest(@RequestBody YeuCauTuyenDung request) {
        try {
            if (request.getIdYeuCau() == null || request.getIdYeuCau().isEmpty()) {
                request.setIdYeuCau("REQ_" + System.currentTimeMillis());
            }
            YeuCauTuyenDung saved = yeuCauRepo.save(request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi gửi yêu cầu: " + e.getMessage());
        }
    }

    @GetMapping("/all-requests")
    public ResponseEntity<List<YeuCauTuyenDung>> getAllRequests() {
        return ResponseEntity.ok(yeuCauRepo.findAll());
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyJob(@RequestBody HoSoUngVien candidate) {
        try {
            HoSoUngVien saved = candidateRepo.save(candidate);
            return ResponseEntity.ok("Nộp hồ sơ thành công! ID ứng viên: " + saved.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi nộp hồ sơ: " + e.getMessage());
        }
    }

    @GetMapping("/candidates")
    public ResponseEntity<?> getAllCandidates() {
        try {
            return ResponseEntity.ok(candidateRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi lấy danh sách ứng viên: " + e.getMessage());
        }
    }

    @PostMapping("/schedule-interview")
    public ResponseEntity<?> scheduleInterview(@RequestBody LichPhongVan schedule) {
        try {
            if (schedule.getIdLich() == null || schedule.getIdLich().isEmpty()) {
                schedule.setIdLich("IVW_" + System.currentTimeMillis());
            }
            LichPhongVan saved = interviewRepo.save(schedule);
            return ResponseEntity.ok("Lên lịch phỏng vấn thành công! ID Lịch: " + saved.getIdLich());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lập lịch phỏng vấn: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{id}")
    @Transactional
    public ResponseEntity<?> approveCandidate(@PathVariable Integer id) {
        try {
            HoSoUngVien candidate = candidateRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ứng viên ID: " + id));
            NhanVien nv = new NhanVien();
            nv.setId("NV" + (System.currentTimeMillis() / 1000));
            nv.setHoTen(candidate.getHoTen());
            nv.setEmailCongViec(candidate.getEmail());
            nv.setSoDienThoai(candidate.getSoDienThoai());
            nv.setNgayVaoLam(new Date());
            nv.setTrangThaiHoatDong("DANG_LAM_VIEC");
            nv.setHeSoLuong(1.0f);
            if (candidate.getYeuCauTuyenDung() != null) {
                nv.setPhongBan(candidate.getYeuCauTuyenDung().getPhongBan());
            }
            nhanVienRepo.save(nv);
            candidate.setTrangThai("TRUNG_TUYEN");
            candidateRepo.save(candidate);
            return ResponseEntity.ok("Đã phê duyệt Onboarding thành công cho: " + nv.getHoTen());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi Onboarding: " + e.getMessage());
        }
    }

    // --- BỔ SUNG CÁC CHỨC NĂNG CÒN THIẾU (CRUD) ---

    // Cập nhật thông tin ứng viên
    @PutMapping("/candidate/{id}")
    public ResponseEntity<?> updateCandidate(@PathVariable Integer id, @RequestBody HoSoUngVien details) {
        return candidateRepo.findById(id).map(uv -> {
            uv.setHoTen(details.getHoTen());
            uv.setEmail(details.getEmail());
            uv.setSoDienThoai(details.getSoDienThoai());
            uv.setTrangThai(details.getTrangThai());
            return ResponseEntity.ok(candidateRepo.save(uv));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Xóa hồ sơ ứng viên
    @DeleteMapping("/candidate/{id}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Integer id) {
        try {
            candidateRepo.deleteById(id);
            return ResponseEntity.ok("Đã xóa ứng viên ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }

    // Xóa yêu cầu tuyển dụng
    @DeleteMapping("/hiring-request/{id}")
    public ResponseEntity<?> deleteHiringRequest(@PathVariable String id) {
        try {
            yeuCauRepo.deleteById(id);
            return ResponseEntity.ok("Đã xóa yêu cầu: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa: " + e.getMessage());
        }
    }
}