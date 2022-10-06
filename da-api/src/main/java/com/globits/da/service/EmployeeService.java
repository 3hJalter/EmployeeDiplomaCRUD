package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public interface EmployeeService {
    Response<List<EmployeeDto>> findAll();

    Response<Employee> save(EmployeeDto employeeDto);

    Response<?> deleteById(UUID uuid);

    Response<Employee> updateById(EmployeeDto employeeDto, UUID uuid);

    Response<EmployeeDto> findById(UUID uuid);

    Response<?> exportExcel(HttpServletResponse response);

    Response<?> importExcel(MultipartFile file);

}
