package com.globits.da.service;

import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.utils.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProvinceService {

    Response<List<ProvinceDto>> findAll();

    Response<Province> save(ProvinceDto provinceDto);

    Response<?> deleteById(UUID uuid);

    Response<?> updateById(ProvinceDto provinceDto, UUID uuid);

    Response<ProvinceDto> findById(UUID uuid);

}
