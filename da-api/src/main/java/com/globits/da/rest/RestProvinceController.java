package com.globits.da.rest;

import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RestProvinceController {

    private final ProvinceService provinceService;
    private final DistrictService districtService;

    @Autowired
    public RestProvinceController(ProvinceService provinceService, DistrictService districtService) {
        this.provinceService = provinceService;
        this.districtService = districtService;
    }

    @GetMapping("/provinces")
    public Response<List<ProvinceDto>> findAll() {
        return provinceService.findAll();
    }

    @GetMapping("/province/{uuid}/districts")
    public Response<List<DistrictDto>> findDistrictListByProvinceID(@PathVariable UUID uuid) {
        return districtService.findDistrictListByProvinceID(uuid);
    }

    @GetMapping("/province/{uuid}")
    public Response<ProvinceDto> findById(@PathVariable UUID uuid) {
        return provinceService.findById(uuid);
    }

    @PostMapping("/provinces")
    public Response<Province> save(@RequestBody ProvinceDto provinceDto) {
        return provinceService.save(provinceDto);
    }

    @DeleteMapping("/province/{uuid}")
    public Response<?> deleteById(@PathVariable UUID uuid) {
        return provinceService.deleteById(uuid);
    }

    @PutMapping("/province/{uuid}")
    public Response<?> updateById(@RequestBody ProvinceDto provinceDto, @PathVariable UUID uuid) {
        return provinceService.updateById(provinceDto, uuid);
    }
}
