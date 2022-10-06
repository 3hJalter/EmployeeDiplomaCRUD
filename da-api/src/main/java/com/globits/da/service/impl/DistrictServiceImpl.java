package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CommuneService;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.Response;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.DistrictResponseMessage;
import com.globits.da.utils.responseMessageImpl.ProvinceResponseMessage;
import com.globits.da.validation.DistrictValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictValidation districtValidation;
    private final CommuneService communeService;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository,
                               ProvinceRepository provinceRepository, DistrictValidation districtValidation,
                               CommuneService communeService) {
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.districtValidation = districtValidation;
        this.communeService = communeService;
    }

    @Override
    public Response<List<DistrictDto>> findAll() {
        List<DistrictDto> districtDtoList = districtRepository.findAll()
                .stream()
                .map(district -> modelMapper.map(district, DistrictDto.class))
                .collect(Collectors.toList());
        return new Response<>(districtDtoList, DistrictResponseMessage.SUCCESSFUL);
    }

    @Override
    public Response<DistrictDto> findById(UUID uuid) {
        if (districtRepository.existsById(uuid)) {
            DistrictDto districtDto = modelMapper.map(districtRepository.getOne(uuid), DistrictDto.class);
            return new Response<>(districtDto, DistrictResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, DistrictResponseMessage.DISTRICT_DOES_NOT_EXIST);
    }

    @Override
    public Response<District> save(DistrictDto districtDto) {
        District district = new District();
        ResponseMessage responseMessage = districtValidation.validate(districtDto, null);
        if (responseMessage == DistrictResponseMessage.SUCCESSFUL) {
            convertDtoToEntity(districtDto, district);
            districtRepository.save(district);
            return new Response<>(district, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<?> deleteById(UUID uuid) {
        if (districtValidation.isExists(uuid)) {
            districtRepository.deleteById(uuid);
            return new Response<>(null, DistrictResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, DistrictResponseMessage.DISTRICT_DOES_NOT_EXIST);
    }

    @Override
    public Response<?> updateById(DistrictDto districtDto, UUID uuid) {
        if (!districtValidation.isExists(uuid)) {
            return new Response<>(null, DistrictResponseMessage.DISTRICT_DOES_NOT_EXIST);
        }
        ResponseMessage responseMessage = districtValidation.validate(districtDto, uuid);
        if (responseMessage == DistrictResponseMessage.SUCCESSFUL) {
            District district = districtRepository.getDistrictById(uuid);
            convertDtoToEntity(districtDto, district);
            districtRepository.save(district);
            return new Response<>(district, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<List<DistrictDto>> findDistrictListByProvinceID(UUID uuid) {
        if (provinceRepository.existsById(uuid)) {
            Province province = provinceRepository.getProvinceById(uuid);
            List<District> districtList = province.getDistrictList();
            if (districtList.isEmpty()) {
                return new Response<>(null, ProvinceResponseMessage.PROVINCE_DOES_NOT_HAVE_DISTRICT);
            }
            List<DistrictDto> districtDtoList = districtList.stream()
                    .map(district -> modelMapper.map(district, DistrictDto.class))
                    .collect(Collectors.toList());
            return new Response<>(districtDtoList, ProvinceResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, ProvinceResponseMessage.PROVINCE_DOES_NOT_EXIST);
    }

    public List<District> addDistrictListToProvince(ProvinceDto provinceDto, Province province) {
        List<District> districtList = new ArrayList<>();
        for (DistrictDto districtDto : provinceDto.getDistrictList()) {
            District district;
            if (!districtRepository.existsDistrictByCode(districtDto.getCode())) {
                district = new District();
                district.setCode(districtDto.getCode());
                district.setName(districtDto.getName());
                district.setProvince(province);
                if (!districtDto.getCommuneList().isEmpty()) {
                    district.setCommuneList(communeService.addCommuneListToDistrict(districtDto, district));
                }
            } else {
                district = districtRepository.getDistrictByCode(districtDto.getCode());
                if (!district.getName().equals(districtDto.getName())) {
                    district.setName(districtDto.getName());
                }
                if (!district.getCode().equals(districtDto.getCode())) {
                    district.setCode(districtDto.getCode());
                }
                if (!district.getProvince().getId().equals(province.getId())) {
                    district.setProvince(province);
                }
            }
            districtList.add(district);
        }
        return districtList;
    }

    public void convertDtoToEntity(DistrictDto districtDto, District district) {
        district.setName(districtDto.getName());
        district.setCode(districtDto.getCode());
        if (districtDto.getProvince() != null
                && provinceRepository.existsById(districtDto.getProvince().getId())) {
            Province province = provinceRepository.getProvinceById(districtDto.getProvince().getId());
            district.setProvince(province);
        }
        if (!districtDto.getCommuneList().isEmpty()) {
            district.setCommuneList(communeService.addCommuneListToDistrict(districtDto, district));
        }
    }

}
