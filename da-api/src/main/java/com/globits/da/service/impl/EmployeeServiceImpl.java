package com.globits.da.service.impl;

import com.globits.da.Constants;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.*;
import com.globits.da.utils.responseMessageImpl.EmployeeResponseMessage;
import com.globits.da.validation.EmployeeValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final ModelMapper modelMapper = new ModelMapper();
    private final EmployeeRepository employeeRepository;
    private final CommuneRepository communeRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final EmployeeValidation employeeValidation;
    private final ImportExportExcelUtil importExportExcelUtil;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CommuneRepository communeRepository,
                               DistrictRepository districtRepository, ProvinceRepository provinceRepository,
                               EmployeeValidation employeeValidation, ImportExportExcelUtil importExportExcelUtil) {
        this.employeeRepository = employeeRepository;
        this.communeRepository = communeRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.employeeValidation = employeeValidation;
        this.importExportExcelUtil = importExportExcelUtil;
    }

    public void convertDtoToEntity(EmployeeDto employeeDto, Employee employee) {
        employee.setName(employeeDto.getName());
        employee.setCode(employeeDto.getCode());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setAge(employeeDto.getAge());
        if (employeeDto.getDistrict() != null &&
                districtRepository.existsById(employeeDto.getDistrict().getId())) {
            District district = districtRepository.getDistrictById(employeeDto.getDistrict().getId());
            employee.setDistrict(modelMapper.map(district, District.class));
        }
        if (employeeDto.getCommune() != null &&
                communeRepository.existsById(employeeDto.getCommune().getId())) {
            Commune commune = communeRepository.getCommuneById(employeeDto.getCommune().getId());
            employee.setCommune(modelMapper.map(commune, Commune.class));
        }
        if (employeeDto.getProvince() != null &&
                provinceRepository.existsById(employeeDto.getProvince().getId())) {
            Province province = provinceRepository.getProvinceById(employeeDto.getProvince().getId());
            employee.setProvince(modelMapper.map(province, Province.class));
        }
    }

    @Override
    public Response<List<EmployeeDto>> findAll() {
        List<EmployeeDto> employeeDtoList = employeeRepository.findAll()
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
        return new Response<>(employeeDtoList, EmployeeResponseMessage.SUCCESSFUL);
    }

    @Override
    public Response<Employee> save(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        ResponseMessage responseMessage = employeeValidation.validate(employeeDto, null);
        if (responseMessage == EmployeeResponseMessage.SUCCESSFUL) {
            convertDtoToEntity(employeeDto, employee);
            employeeRepository.save(employee);
            return new Response<>(employee, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<?> deleteById(UUID uuid) {
        if (employeeValidation.isExists(uuid)) {
            employeeRepository.deleteById(uuid);
            return new Response<>(null, EmployeeResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, EmployeeResponseMessage.EMPLOYEE_DOES_NOT_EXIST);
    }

    @Override
    public Response<Employee> updateById(EmployeeDto employeeDto, UUID uuid) {
        if (!employeeValidation.isExists(uuid)) {
            return new Response<>(null, EmployeeResponseMessage.EMPLOYEE_DOES_NOT_EXIST);
        }
        ResponseMessage responseMessage = employeeValidation.validate(employeeDto, uuid);
        if (responseMessage == EmployeeResponseMessage.SUCCESSFUL) {
            Employee employee = employeeRepository.getEmployeeById(uuid);
            convertDtoToEntity(employeeDto, employee);
            employeeRepository.save(employee);
            return new Response<>(employee, responseMessage);
        }
        return new Response<>(null, responseMessage);
    }

    @Override
    public Response<EmployeeDto> findById(UUID uuid) {
        if (employeeValidation.isExists(uuid)) {
            EmployeeDto employeeDto = modelMapper.map(employeeRepository.getEmployeeById(uuid), EmployeeDto.class);
            return new Response<>(employeeDto, EmployeeResponseMessage.SUCCESSFUL);
        }
        return new Response<>(null, EmployeeResponseMessage.EMPLOYEE_DOES_NOT_EXIST);
    }

    @Override
    public Response<?> exportExcel(HttpServletResponse response) {
        List<Employee> employeeList = employeeRepository.findAll();
        response.setContentType(Constants.CONTENT_TYPE);
        response.setHeader(Constants.HEADER_KEY, Constants.HEADER_VALUE);
        return new Response<>(null, importExportExcelUtil.writeExcel(employeeList, response));
    }

    @Override
    public Response<?> importExcel(MultipartFile file) {
        List<String> errorList = new ArrayList<>();
        return new Response<>(errorList, importExportExcelUtil.readExcel(file, errorList));
    }

}
