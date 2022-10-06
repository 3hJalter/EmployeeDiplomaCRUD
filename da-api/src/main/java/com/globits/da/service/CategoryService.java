package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Category;
import com.globits.da.dto.CategoryDto;
import com.globits.da.dto.search.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService extends GenericService<Category, UUID> {
    Page<CategoryDto> getPage(int pageSize, int pageIndex);

    CategoryDto saveOrUpdate(UUID id, CategoryDto dto);

    Boolean deleteKho(UUID id);

    CategoryDto getCertificate(UUID id);

    Page<CategoryDto> searchByPage(SearchDto dto);

    Boolean checkCode(UUID id, String code);

    List<CategoryDto> getAllCategory();

    Boolean deleteCheckById(UUID id);
}
