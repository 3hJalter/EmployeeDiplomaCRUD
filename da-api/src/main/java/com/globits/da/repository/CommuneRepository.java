package com.globits.da.repository;

import com.globits.da.domain.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, UUID> {

    Commune getCommuneById(UUID id);

    Commune findCommuneById(UUID id);

    Commune getCommuneByCode(String code);

    boolean existsCommuneByCode(String code);
}
