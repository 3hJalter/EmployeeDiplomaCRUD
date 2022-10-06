package com.globits.da.repository;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {

    District getDistrictById(UUID uuid);

    District findDistrictById(UUID uuid);

    District getDistrictByCode(String code);

    boolean existsDistrictByCode(String code);

    @Query("SELECT c FROM Commune AS c, District AS d " +
            "WHERE d.id = c.district.id AND c.code=?1 AND d.id=?2")
    Commune getCommuneByItsCodeAndDistrictId(String code, UUID uuid);
}
