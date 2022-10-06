package com.globits.da.rest;

import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.service.CommuneService;
import com.globits.da.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RestCommuneController {
    private final CommuneService communeService;

    @Autowired
    public RestCommuneController(CommuneService communeService) {
        this.communeService = communeService;
    }

    @GetMapping("/communes")
    public Response<List<CommuneDto>> findAll() {
        return communeService.findAll();
    }

    @GetMapping("/commune/{uuid}")
    public Response<CommuneDto> findById(@PathVariable UUID uuid) {
        return communeService.findById(uuid);
    }

    @PostMapping("/communes")
    public Response<Commune> save(@RequestBody CommuneDto communeDto) {
        return communeService.save(communeDto);
    }

    @DeleteMapping("/commune/{uuid}")
    public Response<?> deleteById(@PathVariable UUID uuid) {
        return communeService.deleteById(uuid);
    }

    @PutMapping("/commune/{uuid}")
    public Response<Commune> updateById(@RequestBody CommuneDto communeDto, @PathVariable UUID uuid) {
        return communeService.updateById(communeDto, uuid);
    }
}
