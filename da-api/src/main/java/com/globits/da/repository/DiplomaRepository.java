package com.globits.da.repository;

import com.globits.da.domain.Diploma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiplomaRepository extends JpaRepository<Diploma, UUID> {
    Diploma getDiplomaById(UUID id);
}
