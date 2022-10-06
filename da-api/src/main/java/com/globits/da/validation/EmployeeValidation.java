package com.globits.da.validation;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.EmployeeResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeValidation {
    private final EmployeeRepository employeeRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;

    @Autowired
    public EmployeeValidation(EmployeeRepository employeeRepository, DistrictRepository districtRepository,
                              ProvinceRepository provinceRepository, CommuneRepository communeRepository) {
        this.employeeRepository = employeeRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.communeRepository = communeRepository;
    }

    public boolean isExists(UUID uuid) {
        return employeeRepository.existsById(uuid);
    }

    public boolean isCodeDuplicated(EmployeeDto employeeDto, UUID uuid) {
        if (uuid == null) {
            return employeeRepository.existsEmployeeByCode(employeeDto.getCode());
        }
        if (employeeRepository.existsEmployeeByCode(employeeDto.getCode())) {
            return (!employeeRepository.getEmployeeByCode(employeeDto.getCode()).getId().equals(uuid));
        }
        return false;
    }

    public boolean hasProvince(EmployeeDto employeeDto) {
        if (employeeDto.getProvince() == null) {
            UUID uuid = employeeDto.getProvince().getId();
            return uuid != null && provinceRepository.existsById(uuid);
        }
        return false;
    }

    public boolean hasDistrict(EmployeeDto employeeDto) {
        if (employeeDto.getDistrict() != null) {
            UUID uuid = employeeDto.getDistrict().getId();
            return uuid != null && districtRepository.existsById(uuid);
        }
        return false;
    }

    public boolean hasCommune(EmployeeDto employeeDto) {
        if (employeeDto.getCommune() != null) {
            UUID uuid = employeeDto.getCommune().getId();
            return uuid != null && communeRepository.existsById(uuid);
        }
        return false;
    }

    public ResponseMessage hasAddress(EmployeeDto employeeDto) {
        if (!hasProvince(employeeDto)) {
            return EmployeeResponseMessage.EMPLOYEE_DOES_NOT_HAVE_PROVINCE;
        }
        UUID uuid = employeeDto.getProvince().getId();
        Province province = provinceRepository.getProvinceById(uuid);
        if (!hasDistrict(employeeDto)) {
            return EmployeeResponseMessage.EMPLOYEE_DOES_NOT_HAVE_DISTRICT;
        }
        uuid = employeeDto.getDistrict().getId();
        District district = districtRepository.getDistrictById(uuid);
        if (district.getProvince().getId() != province.getId()) {
            return EmployeeResponseMessage.DISTRICT_DOES_NOT_BELONG_TO_PROVINCE;
        }
        if (!hasCommune(employeeDto)) {
            return EmployeeResponseMessage.EMPLOYEE_DOES_NOT_HAVE_COMMUNE;
        }
        uuid = employeeDto.getCommune().getId();
        Commune commune = communeRepository.getCommuneById(uuid);
        if (commune.getDistrict().getId() != district.getId()) {
            return EmployeeResponseMessage.COMMUNE_DOES_NOT_BELONG_TO_DISTRICT;
        }
        return EmployeeResponseMessage.SUCCESSFUL;
    }

    public ResponseMessage validate(EmployeeDto employeeDto, UUID uuid) {
        if (employeeDto.getCode() == null)
            return EmployeeResponseMessage.EMPLOYEE_CODE_IS_NULL;
        if (GeneralValidation.hasWhiteSpace(employeeDto.getCode()))
            return EmployeeResponseMessage.EMPLOYEE_CODE_HAS_WHITESPACE;
        if (!GeneralValidation.isCodeLengthValid(employeeDto.getCode()))
            return EmployeeResponseMessage.EMPLOYEE_CODE_LENGTH_IS_INVALID;
        if (isCodeDuplicated(employeeDto, uuid))
            return EmployeeResponseMessage.EMPLOYEE_CODE_IS_DUPLICATED;
        if (employeeDto.getName() == null)
            return EmployeeResponseMessage.EMPLOYEE_NAME_IS_NULL;
        if (!GeneralValidation.isNameValid(employeeDto.getName()))
            return EmployeeResponseMessage.EMPLOYEE_NAME_IS_INVALID;
        if (employeeDto.getEmail() == null)
            return EmployeeResponseMessage.EMPLOYEE_EMAIL_IS_NULL;
        if (GeneralValidation.hasWhiteSpace(employeeDto.getEmail()))
            return EmployeeResponseMessage.EMPLOYEE_EMAIL_HAS_WHITESPACES;
        if (!GeneralValidation.isEmailValid(employeeDto.getEmail()))
            return EmployeeResponseMessage.EMPLOYEE_EMAIL_IS_INVALID;
        if (employeeDto.getPhone() == null)
            return EmployeeResponseMessage.EMPLOYEE_PHONE_IS_NULL;
        if (!GeneralValidation.isPhoneValid(employeeDto.getPhone()))
            return EmployeeResponseMessage.EMPLOYEE_PHONE_IS_INVALID;
        if (employeeDto.getAge() == null)
            return EmployeeResponseMessage.EMPLOYEE_AGE_IS_NULL;
        if (!GeneralValidation.isAgeValid(employeeDto.getAge()))
            return EmployeeResponseMessage.EMPLOYEE_AGE_NOT_POSITIVE;
        return hasAddress(employeeDto);
    }
}
