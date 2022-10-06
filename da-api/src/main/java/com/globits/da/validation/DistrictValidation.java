package com.globits.da.validation;

import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.CommuneResponseMessage;
import com.globits.da.utils.responseMessageImpl.DistrictResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DistrictValidation {
    private final DistrictRepository districtRepository;
    private final ProvinceValidation provinceValidation;
    private final CommuneValidation communeValidation;

    @Autowired
    @Lazy
    public DistrictValidation(DistrictRepository districtRepository, ProvinceValidation provinceValidation,
                              CommuneValidation communeValidation) {
        this.districtRepository = districtRepository;
        this.provinceValidation = provinceValidation;
        this.communeValidation = communeValidation;
    }

    public boolean isExists(UUID uuid) {
        return districtRepository.existsById(uuid);
    }

    public boolean isCodeDuplicated(DistrictDto districtDto, UUID uuid) {
        if (uuid == null) {
            return districtRepository.existsDistrictByCode(districtDto.getCode());
        }
        if (districtRepository.existsDistrictByCode(districtDto.getCode())) {
            return (!districtRepository.getDistrictByCode(districtDto.getCode()).getId().equals(uuid));
        }
        return false;
    }

    public ResponseMessage validate(DistrictDto districtDto, UUID uuid) {
        if (districtDto.getProvince() != null &&
                provinceValidation.isExists(districtDto.getProvince().getId()))
            return DistrictResponseMessage.DISTRICT_DOES_NOT_HAVE_PROVINCE;
        if (districtDto.getCode() == null)
            return DistrictResponseMessage.DISTRICT_CODE_IS_NULL;
        if (GeneralValidation.hasWhiteSpace(districtDto.getCode()))
            return DistrictResponseMessage.DISTRICT_CODE_HAS_WHITESPACE;
        if (isCodeDuplicated(districtDto, uuid))
            return DistrictResponseMessage.DISTRICT_CODE_IS_DUPLICATED;
        if (districtDto.getName() == null)
            return DistrictResponseMessage.DISTRICT_NAME_IS_NULL;
        return isCommuneListValid(districtDto, uuid);
    }

    private ResponseMessage isCommuneListValid(DistrictDto districtDto, UUID uuid) {
        List<CommuneDto> communeDtoList = districtDto.getCommuneList();
        if (!communeDtoList.isEmpty()) {
            ResponseMessage responseMessage;
            for (CommuneDto communeDto : communeDtoList) {
                responseMessage = communeValidation.validate(communeDto, null);
                if (responseMessage != CommuneResponseMessage.SUCCESSFUL) {
                    return responseMessage;
                }
                if (uuid != null) {
                    Commune commune = districtRepository.getCommuneByItsCodeAndDistrictId(communeDto.getCode(), uuid);
                    if (commune != null) {
                        continue;
                    }
                    return CommuneResponseMessage.COMMUNE_CODE_IS_DUPLICATED;
                }
            }
        }
        return DistrictResponseMessage.SUCCESSFUL;
    }

}
