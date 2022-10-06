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
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto extends BaseObjectDto {
    private String code;
    private String name;
    private List<DistrictDto> districtList = new ArrayList<>();
    private List<DiplomaDto> diplomaList = new ArrayList<>();
}
