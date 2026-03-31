package com.Group117.hrm_system.Repository;

import com.Group117.hrm_system.entity.DonNghiPhep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonNghiPhepRepository extends JpaRepository<DonNghiPhep, Long> {
}