package com.globits.da.service;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.utils.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DistrictService {

    Response<List<DistrictDto>> findAll();

    Response<?> save(DistrictDto districtDto);

    Response<?> deleteById(UUID uuid);

    Response<?> updateById(DistrictDto districtDto, UUID uuid);

    Response<DistrictDto> findById(UUID uuid);

    Response<List<DistrictDto>> findDistrictListByProvinceID(UUID uuid);

    List<District> addDistrictListToProvince(ProvinceDto provinceDto, Province province);
}
