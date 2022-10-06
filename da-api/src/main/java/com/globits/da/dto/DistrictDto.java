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
public class DistrictDto extends BaseObjectDto {
    private String code;
    private String name;
    private ProvinceDto province;
    private List<CommuneDto> communeList = new ArrayList<>();

}
