package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.CommuneService;
import com.globits.da.utils.Response;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.CommuneResponseMessage;
import com.globits.da.utils.responseMessageImpl.DistrictResponseMessage;
import com.globits.da.validation.CommuneValidation;
import com.globits.da.validation.DistrictValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommuneServiceImpl implements CommuneService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final CommuneRepository communeRepository;
    private final DistrictRepository districtRepository;
    private final CommuneValidation communeValidation;
    private final DistrictValidation districtValidation;

    @Autowired
    public CommuneServiceImpl(CommuneRepository communeRepository, DistrictRepository districtRepository,
                              CommuneValidation communeValidation, DistrictValidation districtValidation) {
        this.communeRepository = communeRepository;
        this.districtRepository = districtRepository;
        this.communeValidation = communeValidation;
        this.districtValidation = districtValidation;
    }

    @Override
    public Response<List<CommuneDto>> findAll() {
        List<CommuneDto> communeDtoList = communeRepository.findAll()
                .stream()
                .map(commune -> modelMapper.map(commune, CommuneDto.class))
                .collect(Collectors.toList());
        return new Response<>(communeDtoList, CommuneResponseMessage.SUCCESSFUL);
    }

    @Override
    public Response<CommuneDto> findById(UUID uuid) {
        if (communeValidation.isExists(uuid)) {
            CommuneDto communeDto = modelMapper.map(communeRepository.getCommuneById(uuid), CommuneDto.class);
            return new Response<>(communeDto, CommuneResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, CommuneResponseMessage.COMMUNE_DOES_NOT_EXIST);
    }

    @Override
    public Response<Commune> save(CommuneDto communeDto) {
        Commune commune = new Commune();
        ResponseMessage responseMessage = communeValidation.validate(communeDto, null);
        if (responseMessage == CommuneResponseMessage.SUCCESSFUL) {
            convertDtoToEntity(communeDto, commune);
            communeRepository.save(commune);
            return new Response<>(commune, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<?> deleteById(UUID uuid) {
        if (communeValidation.isExists(uuid)) {
            communeRepository.deleteById(uuid);
            return new Response<>(null, CommuneResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, CommuneResponseMessage.COMMUNE_DOES_NOT_EXIST);
    }

    @Override
    public Response<Commune> updateById(CommuneDto communeDto, UUID uuid) {
        if (!communeValidation.isExists(uuid)) {
            return new Response<>(null, CommuneResponseMessage.COMMUNE_DOES_NOT_EXIST);
        }
        ResponseMessage responseMessage = communeValidation.validate(communeDto, uuid);
        if (responseMessage == CommuneResponseMessage.SUCCESSFUL) {
            Commune commune = communeRepository.getCommuneById(uuid);
            convertDtoToEntity(communeDto, commune);
            communeRepository.save(commune);
            return new Response<>(commune, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<List<CommuneDto>> findCommuneListByDistrictID(UUID uuid) {
        if (districtValidation.isExists(uuid)) {
            District district = districtRepository.getDistrictById(uuid);
            List<Commune> communeList = district.getCommuneList();
            if (communeList.isEmpty()) {
                return new Response<>(null, DistrictResponseMessage.DISTRICT_DOES_NOT_HAVE_COMMUNE);
            }
            List<CommuneDto> communeDtoList = communeList.stream()
                    .map(commune -> modelMapper.map(commune, CommuneDto.class))
                    .collect(Collectors.toList());
            return new Response<>(communeDtoList, DistrictResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, DistrictResponseMessage.DISTRICT_DOES_NOT_EXIST);
    }

    @Override
    public List<Commune> addCommuneListToDistrict(DistrictDto districtDto, District district) {
        List<Commune> communeList = new ArrayList<>();
        for (CommuneDto communeDto : districtDto.getCommuneList()) {
            Commune commune;
            if (!communeRepository.existsCommuneByCode(communeDto.getCode())) {
                commune = new Commune();
                commune.setName(communeDto.getName());
                commune.setCode(communeDto.getCode());
                commune.setDistrict(district);
            } else {
                commune = communeRepository.getCommuneByCode(communeDto.getCode());
                if (!commune.getName().equals(communeDto.getName())) {
                    commune.setName(communeDto.getName());
                }
                if (!commune.getCode().equals(communeDto.getCode())) {
                    commune.setCode(communeDto.getCode());
                }
                if (!commune.getDistrict().getId().equals(district.getId())) {
                    commune.setDistrict(district);
                }
            }
            communeList.add(commune);
        }
        return communeList;
    }

    public void convertDtoToEntity(CommuneDto communeDto, Commune commune) {
        commune.setName(communeDto.getName());
        commune.setCode(communeDto.getCode());
        if (communeDto.getDistrict() != null &&
                districtRepository.existsById(communeDto.getDistrict().getId())) {
            District district = districtRepository.getDistrictById(communeDto.getDistrict().getId());
            commune.setDistrict(modelMapper.map(district, District.class));
        }
    }
}
