package com.globits.da.service.impl;

import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.Response;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.ProvinceResponseMessage;
import com.globits.da.validation.ProvinceValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final ProvinceRepository provinceRepository;
    private final ProvinceValidation provinceValidation;
    private final DistrictService districtService;

    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceValidation provinceValidation,
                               DistrictService districtService) {
        this.provinceRepository = provinceRepository;
        this.provinceValidation = provinceValidation;
        this.districtService = districtService;
    }

    @Override
    public Response<List<ProvinceDto>> findAll() {
        List<ProvinceDto> provinceDtoList = provinceRepository.findAll()
                .stream()
                .map(province -> modelMapper.map(province, ProvinceDto.class))
                .collect(Collectors.toList());
        return new Response<>(provinceDtoList, ProvinceResponseMessage.SUCCESSFUL);
    }

    @Override
    public Response<ProvinceDto> findById(UUID uuid) {
        if (provinceRepository.existsById(uuid)) {
            ProvinceDto provinceDto = modelMapper.map(provinceRepository.getProvinceById(uuid), ProvinceDto.class);
            return new Response<>(provinceDto, ProvinceResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, ProvinceResponseMessage.PROVINCE_DOES_NOT_EXIST);
    }

    @Override
    public Response<Province> save(ProvinceDto provinceDto) {
        Province province = new Province();
        ResponseMessage responseMessage = provinceValidation.validate(provinceDto, null);
        if (responseMessage == ProvinceResponseMessage.SUCCESSFUL) {
            convertDtoToEntity(provinceDto, province);
            provinceRepository.save(province);
            return new Response<>(province, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<?> deleteById(UUID uuid) {
        if (provinceRepository.existsById(uuid)) {
            provinceRepository.deleteById(uuid);
            return new Response<>(null, ProvinceResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, ProvinceResponseMessage.PROVINCE_DOES_NOT_EXIST);
    }

    @Override
    public Response<?> updateById(ProvinceDto provinceDto, UUID uuid) {
        if (!provinceValidation.isExists(uuid)) {
            return new Response<>(null, ProvinceResponseMessage.PROVINCE_DOES_NOT_EXIST);
        }
        ResponseMessage responseMessage = provinceValidation.validate(provinceDto, uuid);
        if (responseMessage == ProvinceResponseMessage.SUCCESSFUL) {
            Province province = provinceRepository.getProvinceById(uuid);
            convertDtoToEntity(provinceDto, province);
            provinceRepository.save(province);
            return new Response<>(province, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    public void convertDtoToEntity(ProvinceDto provinceDto, Province province) {
        province.setName(provinceDto.getName());
        province.setCode(provinceDto.getCode());
        if (!provinceDto.getDistrictList().isEmpty()) {
            province.setDistrictList(districtService.addDistrictListToProvince(provinceDto, province));
        }
    }


}
