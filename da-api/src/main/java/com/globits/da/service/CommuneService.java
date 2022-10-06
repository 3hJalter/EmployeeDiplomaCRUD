package com.globits.da.service;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.utils.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CommuneService {

    Response<List<CommuneDto>> findAll();

    Response<Commune> save(CommuneDto communeDto);

    Response<?> deleteById(UUID uuid);

    Response<Commune> updateById(CommuneDto communeDto, UUID uuid);

    Response<CommuneDto> findById(UUID uuid);

    Response<List<CommuneDto>> findCommuneListByDistrictID(UUID uuid);

    List<Commune> addCommuneListToDistrict(DistrictDto districtDto, District district);

}
