package com.globits.da.rest;

import com.globits.da.domain.Diploma;
import com.globits.da.dto.DiplomaDto;
import com.globits.da.service.DiplomaService;
import com.globits.da.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RestDiplomaController {

    private final DiplomaService diplomaService;

    @Autowired
    public RestDiplomaController(DiplomaService diplomaService) {
        this.diplomaService = diplomaService;
    }

    @GetMapping("/diplomas")
    public Response<List<DiplomaDto>> findAll() {
        return diplomaService.findAll();
    }

    @GetMapping("/diploma/{uuid}")
    public Response<DiplomaDto> findById(@PathVariable UUID uuid) {
        return diplomaService.findById(uuid);
    }

    @PostMapping("/diplomas")
    public Response<Diploma> save(@RequestBody DiplomaDto diplomaDto) {
        return diplomaService.save(diplomaDto);
    }

    @DeleteMapping("/diploma/{uuid}")
    public Response<?> deleteById(@PathVariable UUID uuid) {
        return diplomaService.deleteById(uuid);
    }

    @PutMapping("/diploma/{uuid}")
    public Response<?> updateById(@RequestBody DiplomaDto diplomaDto, @PathVariable UUID uuid) {
        return diplomaService.updateById(diplomaDto, uuid);
    }
}
