package com.Group117.hrm_system.Controller;

import com.Group117.hrm_system.entity.*;
import com.Group117.hrm_system.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService orgService;

    // --- CHI NHÁNH ---
    @GetMapping("/chi-nhanh")
    public List<ChiNhanh> getChiNhanh() { return orgService.getAllChiNhanh(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chi-nhanh")
    public ChiNhanh addChiNhanh(@RequestBody ChiNhanh cn) { return orgService.createChiNhanh(cn); }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/chi-nhanh/{id}")
    public ResponseEntity<?> delChiNhanh(@PathVariable String id) {
        orgService.deleteChiNhanh(id);
        return ResponseEntity.ok("Đã xóa chi nhánh");
    }

    // --- PHÒNG BAN ---
    @GetMapping("/phong-ban")
    public List<PhongBan> getPhongBan() { return orgService.getAllPhongBan(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/phong-ban")
    public PhongBan addPhongBan(@RequestBody PhongBan pb) { return orgService.createPhongBan(pb); }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/phong-ban/{id}")
    public ResponseEntity<?> delPhongBan(@PathVariable String id) {
        orgService.deletePhongBan(id);
        return ResponseEntity.ok("Đã xóa phòng ban");
    }

    // --- NHÓM ---
    @GetMapping("/nhom")
    public List<Nhom> getNhom() { return orgService.getAllNhom(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nhom")
    public Nhom addNhom(@RequestBody Nhom nhom) { return orgService.createNhom(nhom); }

    // --- CHỨC VỤ ---
    @GetMapping("/chuc-vu")
    public List<ChucVu> getChucVu() { return orgService.getAllChucVu(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chuc-vu")
    public ChucVu addChucVu(@RequestBody ChucVu cv) { return orgService.createChucVu(cv); }
}