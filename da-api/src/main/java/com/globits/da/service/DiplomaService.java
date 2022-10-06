package com.globits.da.service;

import com.globits.da.domain.Diploma;
import com.globits.da.dto.DiplomaDto;
import com.globits.da.utils.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DiplomaService {
    Response<List<DiplomaDto>> findAll();

    Response<Diploma> save(DiplomaDto diplomaDto);

    Response<?> deleteById(UUID uuid);

    Response<Diploma> updateById(DiplomaDto diplomaDto, UUID uuid);

    Response<DiplomaDto> findById(UUID uuid);
}
