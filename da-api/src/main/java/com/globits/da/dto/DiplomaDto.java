package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.da.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiplomaDto {
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.FORMAT_PATTERN)
    private LocalDate effectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.FORMAT_PATTERN)
    private LocalDate expirationDate;
    private ProvinceDto province;
    private EmployeeDto employee;
}
