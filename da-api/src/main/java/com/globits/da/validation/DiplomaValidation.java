package com.globits.da.validation;

import com.globits.da.Constants;
import com.globits.da.domain.Diploma;
import com.globits.da.domain.Employee;
import com.globits.da.dto.DiplomaDto;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.DiplomaRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.utils.ResponseMessage;
import com.globits.da.utils.responseMessageImpl.DiplomaResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DiplomaValidation {
    private final DiplomaRepository diplomaRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidation employeeValidation;
    private final ProvinceValidation provinceValidation;

    @Autowired
    public DiplomaValidation(DiplomaRepository diplomaRepository, EmployeeRepository employeeRepository,
                             EmployeeValidation employeeValidation, ProvinceValidation provinceValidation) {
        this.diplomaRepository = diplomaRepository;
        this.employeeRepository = employeeRepository;
        this.employeeValidation = employeeValidation;
        this.provinceValidation = provinceValidation;
    }

    public boolean isExists(UUID uuid) {
        return diplomaRepository.existsById(uuid);
    }

    public ResponseMessage validate(DiplomaDto diplomaDto, UUID uuid) {
        if (diplomaDto.getName() == null)
            return DiplomaResponseMessage.DIPLOMA_NAME_IS_NULL;
        if (diplomaDto.getEffectiveDate() == null)
            return DiplomaResponseMessage.DIPLOMA_EFFECTIVE_DATE_IS_NULL;
        if (!GeneralValidation.isEffectiveDateValid(diplomaDto.getEffectiveDate()))
            return DiplomaResponseMessage.DIPLOMA_EFFECTIVE_DATE_IS_INVALID;
        if (diplomaDto.getExpirationDate() == null)
            return DiplomaResponseMessage.DIPLOMA_EXPIRATION_DATE_IS_NULL;
        if (!GeneralValidation.isExpirationDateValid(diplomaDto.getEffectiveDate(),
                diplomaDto.getExpirationDate()))
            return DiplomaResponseMessage.DIPLOMA_EFFECTIVE_DATE_IS_INVALID;
        if (diplomaDto.getProvince() == null)
            return DiplomaResponseMessage.DIPLOMA_PROVINCE_IS_NULL;
        if (!provinceValidation.isExists(diplomaDto.getProvince().getId())) {
            return DiplomaResponseMessage.DIPLOMA_PROVINCE_DOES_NOT_EXIST;
        }
        return isEmployeeValid(diplomaDto, uuid);
    }

    public ResponseMessage isEmployeeValid(DiplomaDto diplomaDto, UUID uuid) {
        EmployeeDto employeeDto = diplomaDto.getEmployee();
        if (employeeDto == null)
            return DiplomaResponseMessage.DIPLOMA_EMPLOYEE_IS_NULL;
        if (!employeeValidation.isExists(employeeDto.getId())) {
            return DiplomaResponseMessage.DIPLOMA_EMPLOYEE_DOES_NOT_EXIST;
        }
        Employee employee = employeeRepository.getEmployeeById(
                employeeDto.getId());
        List<Diploma> diplomaList = employee.getDiplomaList();
        int count = Constants.MIN_DIPLOMA_NUMBER;
        for (Diploma diploma : diplomaList) {
            if (GeneralValidation.isEffective(diploma.getEffectiveDate())) {
                count++;
                if (count > Constants.MAX_DIPLOMA_NUMBER) {
                    return DiplomaResponseMessage.EMPLOYEE_HAS_MORE_THAN_3_EFFECTIVE_DIPLOMAS;
                }
            }
            if (diploma.getProvince().getId().equals(diplomaDto.getProvince().getId())) {
                if (uuid != null && diploma.getId().equals(uuid)) {
                    continue;
                }
                return DiplomaResponseMessage.EMPLOYEE_HAS_THE_SAME_PROVINCE_DIPLOMA;
            }
        }
        return DiplomaResponseMessage.SUCCESSFUL;
    }

}
