package com.globits.da.validation;

import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.DistrictResponseMessage;
import com.globits.da.utils.responseMessageImpl.ProvinceResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProvinceValidation {
    private final ProvinceRepository provinceRepository;
    private final DistrictValidation districtValidation;

    @Autowired
    public ProvinceValidation(ProvinceRepository provinceRepository, DistrictValidation districtValidation) {
        this.provinceRepository = provinceRepository;
        this.districtValidation = districtValidation;
    }

    public boolean isExists(UUID uuid) {
        return provinceRepository.existsById(uuid);
    }

    public boolean isCodeDuplicated(ProvinceDto provinceDto, UUID uuid) {
        if (uuid == null) {
            return provinceRepository.existsProvinceByCode(provinceDto.getCode());
        }
        if (provinceRepository.existsProvinceByCode(provinceDto.getCode())) {
            UUID check = provinceRepository.getProvinceByCode(provinceDto.getCode()).getId();
            return (!check.equals(uuid));
        }
        return false;
    }

    public ResponseMessage validate(ProvinceDto provinceDto, UUID uuid) {
        if (provinceDto.getCode() == null)
            return ProvinceResponseMessage.PROVINCE_CODE_IS_NULL;
        if (GeneralValidation.hasWhiteSpace(provinceDto.getCode()))
            return ProvinceResponseMessage.PROVINCE_CODE_HAS_WHITESPACE;
        if (isCodeDuplicated(provinceDto, uuid)) {
            return ProvinceResponseMessage.PROVINCE_CODE_IS_DUPLICATED;
        }
        if (provinceDto.getName() == null)
            return ProvinceResponseMessage.PROVINCE_NAME_IS_NULL;
        return isProvinceListValid(provinceDto, uuid);
    }

    private ResponseMessage isProvinceListValid(ProvinceDto provinceDto, UUID uuid) {
        List<DistrictDto> districtDtoList = provinceDto.getDistrictList();
        if (!districtDtoList.isEmpty()) {
            ResponseMessage responseMessage;
            for (DistrictDto districtDto : districtDtoList) {
                responseMessage = districtValidation.validate(districtDto, null);
                if (responseMessage != DistrictResponseMessage.SUCCESSFUL) {
                    return responseMessage;
                }
                if (uuid != null) {
                    District district = provinceRepository.getDistrictByItsCodeAndProvinceId(districtDto.getCode(), uuid);
                    if (district != null) {
                        continue;
                    }
                    return DistrictResponseMessage.DISTRICT_CODE_IS_DUPLICATED;
                }
            }
        }
        return ProvinceResponseMessage.SUCCESSFUL;
    }
}
