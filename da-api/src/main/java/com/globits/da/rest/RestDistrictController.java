package com.globits.da.rest;

import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.service.CommuneService;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RestDistrictController {
    private final DistrictService districtService;
    private final CommuneService communeService;

    @Autowired
    public RestDistrictController(DistrictService districtService, CommuneService communeService) {
        this.districtService = districtService;
        this.communeService = communeService;
    }

    @GetMapping("/districts")
    public Response<List<DistrictDto>> findAll() {
        return districtService.findAll();
    }

    @GetMapping("/district/{uuid}/communes")
    public Response<List<CommuneDto>> findCommuneListByDistrictID(@PathVariable UUID uuid) {
        return communeService.findCommuneListByDistrictID(uuid);
    }

    @GetMapping("/district/{uuid}")
    public Response<DistrictDto> findById(@PathVariable UUID uuid) {
        return districtService.findById(uuid);
    }

    @PostMapping("/districts")
    public Response<?> save(@RequestBody DistrictDto districtDto) {
        return districtService.save(districtDto);
    }

    @DeleteMapping("/district/{uuid}")
    public Response<?> deleteById(@PathVariable UUID uuid) {
        return districtService.deleteById(uuid);
    }

    @PutMapping("/district/{uuid}")
    public Response<?> updateById(@RequestBody DistrictDto districtDto, @PathVariable UUID uuid) {
        return districtService.updateById(districtDto, uuid);
    }
}
