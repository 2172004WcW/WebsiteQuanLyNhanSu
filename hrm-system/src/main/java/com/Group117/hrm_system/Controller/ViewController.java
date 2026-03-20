package com.Group117.hrm_system.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/quan-ly-chuc-vu")
    public String getChucVuPage() {
        return "chuc-vu"; // Trả về file chuc-vu.html trong thư mục templates
    }
    @GetMapping("/them-nhan-vien")
    public String getThemNhanVienPage() {
        return "them-nhan-vien"; // Sẽ tìm file them-nhan-vien.html trong templates
    }
    @GetMapping("/phong-ban")
    public String getPhongBanPage() {
        return "phong-ban"; // Trả về file phong-ban.html trong thư mục templates
    }
    @GetMapping("/quan-ly-nhan-vien")
    public String getQuanLyNhanVienPage() {
        return "quan-ly-nhan-vien";
    }
    @GetMapping("/chi-nhanh")
    public String getChiNhanhPage() { return "chi-nhanh"; }

    @GetMapping("/nhom")
    public String getNhomPage() { return "nhom"; }

    @GetMapping("/ho-so-nhan-vien")
    public String getHoSoNhanVienPage() {
        return "ho-so-nhan-vien";
    }
    @GetMapping("/ban-hanh-quyet-dinh")
    public String getBanHanhQuyetDinhPage() {
        return "ban-hanh-quyet-dinh"; // Tên file phải khớp chính xác với ban-hanh-quyet-dinh.html
    }
}