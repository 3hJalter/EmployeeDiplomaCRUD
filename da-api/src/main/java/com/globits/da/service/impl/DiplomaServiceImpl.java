package com.globits.da.service.impl;

import com.globits.da.domain.Diploma;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.DiplomaDto;
import com.globits.da.repository.DiplomaRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DiplomaService;
import com.globits.da.utils.Response;
import com.globits.da.utils.responseMessageImpl.DiplomaResponseMessage;
import com.globits.da.validation.DiplomaValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiplomaServiceImpl implements DiplomaService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final DiplomaRepository diplomaRepository;
    private final ProvinceRepository provinceRepository;
    private final EmployeeRepository employeeRepository;
    private final DiplomaValidation diplomaValidation;

    @Autowired
    public DiplomaServiceImpl(DiplomaRepository diplomaRepository, ProvinceRepository provinceRepository,
                              EmployeeRepository employeeRepository, DiplomaValidation diplomaValidation) {
        this.diplomaRepository = diplomaRepository;
        this.provinceRepository = provinceRepository;
        this.employeeRepository = employeeRepository;
        this.diplomaValidation = diplomaValidation;
    }

    @Override
    public Response<List<DiplomaDto>> findAll() {
        List<DiplomaDto> diplomaDtoList = diplomaRepository.findAll()
                .stream()
                .map(diploma -> modelMapper.map(diploma, DiplomaDto.class))
                .collect(Collectors.toList());
        return new Response<>(diplomaDtoList, DiplomaResponseMessage.SUCCESSFUL);
    }

    @Override
    public Response<Diploma> save(DiplomaDto diplomaDto) {
        Diploma diploma = new Diploma();
        if (diplomaValidation.validate(diplomaDto, null) == DiplomaResponseMessage.SUCCESSFUL) {
            convertDtoToEntity(diplomaDto, diploma);
            diplomaRepository.save(diploma);
            return new Response<>(diploma, DiplomaResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, diplomaValidation.validate(diplomaDto, null));
    }

    @Override
    public Response<Diploma> updateById(DiplomaDto diplomaDto, UUID uuid) {
        if (!diplomaValidation.isExists(uuid)) {
            return new Response<>(null, DiplomaResponseMessage.DIPLOMA_DOES_NOT_EXIST);
        }
        if (diplomaValidation.validate(diplomaDto, uuid) == DiplomaResponseMessage.SUCCESSFUL) {
            Diploma diploma = diplomaRepository.getDiplomaById(uuid);
            convertDtoToEntity(diplomaDto, diploma);
            diplomaRepository.save(diploma);
            return new Response<>(diploma, DiplomaResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, diplomaValidation.validate(diplomaDto, uuid));
    }

    @Override
    public Response<?> deleteById(UUID uuid) {
        if (diplomaValidation.isExists(uuid)) {
            diplomaRepository.deleteById(uuid);
            return new Response<>(null, DiplomaResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, DiplomaResponseMessage.DIPLOMA_DOES_NOT_EXIST);
    }

    @Override
    public Response<DiplomaDto> findById(UUID uuid) {
        if (diplomaValidation.isExists(uuid)) {
            DiplomaDto diplomaDto = modelMapper.map(diplomaRepository.getDiplomaById(uuid), DiplomaDto.class);
            return new Response<>(diplomaDto, DiplomaResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, DiplomaResponseMessage.DIPLOMA_DOES_NOT_EXIST);
    }

    public void convertDtoToEntity(DiplomaDto diplomaDto, Diploma diploma) {
        diploma.setName(diplomaDto.getName());
        diploma.setEffectiveDate(diplomaDto.getEffectiveDate());
        diploma.setExpirationDate(diplomaDto.getExpirationDate());
        if (diplomaDto.getProvince() != null &&
                provinceRepository.existsById(diplomaDto.getProvince().getId())) {
            Province province = provinceRepository.getProvinceById(diplomaDto.getProvince().getId());
            diploma.setProvince(modelMapper.map(province, Province.class));
        }
        if (diplomaDto.getEmployee() != null &&
                employeeRepository.existsById(diplomaDto.getEmployee().getId())) {
            Employee employee = employeeRepository.getEmployeeById(diplomaDto.getEmployee().getId());
            diploma.setEmployee(modelMapper.map(employee, Employee.class));
        }
    }
}
