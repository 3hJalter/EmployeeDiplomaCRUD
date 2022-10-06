package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RestEmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public RestEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public Response<List<EmployeeDto>> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employee/{uuid}")
    public Response<EmployeeDto> findById(@PathVariable UUID uuid) {
        return employeeService.findById(uuid);
    }

    @PostMapping("/employees")
    public Response<Employee> save(@RequestBody EmployeeDto employeeDto) {
        return employeeService.save(employeeDto);
    }

    @DeleteMapping("/employee/{uuid}")
    public Response<?> deleteById(@PathVariable UUID uuid) {
        return employeeService.deleteById(uuid);
    }

    @PutMapping("/employee/{uuid}")
    public Response<Employee> updateById(@RequestBody EmployeeDto employeeDto, @PathVariable UUID uuid) {
        return employeeService.updateById(employeeDto, uuid);
    }

    @GetMapping("/employees/export-excel")
    public Response<?> exportExcel(HttpServletResponse response) {
        return employeeService.exportExcel(response);
    }

    @PostMapping("/employees/import-excel")
    public Response<?> importExcel(@RequestParam("file")MultipartFile file) {
        return employeeService.importExcel(file);
    }

}
