package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto extends BaseObjectDto {
    private String name;
    private String code;
    private String email;
    private String phone;
    private Integer age;
    private ProvinceDto province;
    private DistrictDto district;
    private CommuneDto commune;
    private List<DiplomaDto> diplomaList = new ArrayList<>();
}
