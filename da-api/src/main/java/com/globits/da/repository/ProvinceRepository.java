package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    Province getProvinceById(UUID id);

    Province findProvincesById(UUID id);

    boolean existsProvinceByCode(String code);

    Province getProvinceByCode(String code);

    @Query("SELECT d FROM District AS d, Province AS p " +
            "WHERE p.id = d.province.id AND d.code=?1 AND p.id=?2")
    District getDistrictByItsCodeAndProvinceId(String code, UUID uuid);
}
