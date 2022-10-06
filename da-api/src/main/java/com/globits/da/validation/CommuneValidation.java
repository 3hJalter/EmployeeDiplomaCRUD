package com.globits.da.validation;

import com.globits.da.dto.CommuneDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.CommuneResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommuneValidation {

    private final CommuneRepository communeRepository;
    private final DistrictValidation districtValidation;

    @Autowired
    public CommuneValidation(CommuneRepository communeRepository, DistrictValidation districtValidation) {
        this.communeRepository = communeRepository;
        this.districtValidation = districtValidation;
    }

    public boolean isExists(UUID uuid) {
        return communeRepository.existsById(uuid);
    }

    public boolean isCodeDuplicated(CommuneDto communeDto, UUID uuid) {
        if (uuid == null) {
            return communeRepository.existsCommuneByCode(communeDto.getCode());
        }
        if (communeRepository.existsCommuneByCode(communeDto.getCode())) {
            return (!communeRepository.getCommuneByCode(communeDto.getCode()).getId().equals(uuid));
        }
        return false;
    }

    public ResponseMessage validate(CommuneDto communeDto, UUID uuid) {
        if (communeDto.getDistrict() != null &&
                districtValidation.isExists(communeDto.getDistrict().getId()))
            return CommuneResponseMessage.COMMUNE_DOES_NOT_HAVE_DISTRICT;
        if (communeDto.getCode() == null)
            return CommuneResponseMessage.COMMUNE_CODE_IS_NULL;
        if (GeneralValidation.hasWhiteSpace(communeDto.getCode()))
            return CommuneResponseMessage.COMMUNE_CODE_HAS_WHITESPACE;
        if (isCodeDuplicated(communeDto, uuid))
            return CommuneResponseMessage.COMMUNE_CODE_IS_DUPLICATED;
        if (communeDto.getName() == null)
            return CommuneResponseMessage.COMMUNE_NAME_IS_NULL;
        return CommuneResponseMessage.SUCCESSFUL;
    }
}
