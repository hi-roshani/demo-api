package com.wagh.demo.api.service;

import com.wagh.demo.api.dto.webhook.TemplateCategoryDTO;
import com.wagh.demo.api.model.TemplateCategory;
import com.wagh.demo.api.repo.TemplateCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemplateCategoryService {

    @Autowired
    private TemplateCategoryRepository templateCategoryRepository;

    public List<TemplateCategoryDTO> findAllCategories() {
        return templateCategoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TemplateCategoryDTO> findCategoryById(Long id) {
        return templateCategoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    public TemplateCategoryDTO addCategory(TemplateCategoryDTO templateCategoryDTO) {
        TemplateCategory templateCategory = convertToEntity(templateCategoryDTO);
        TemplateCategory savedCategory = templateCategoryRepository.save(templateCategory);
        return convertToDTO(savedCategory);
    }

    public TemplateCategoryDTO updateCategory(Long id, TemplateCategoryDTO templateCategoryDTO) {
        if (templateCategoryRepository.existsById(id)) {
            TemplateCategory templateCategory = convertToEntity(templateCategoryDTO);
            templateCategory.setId(id);
            TemplateCategory updatedCategory = templateCategoryRepository.save(templateCategory);
            return convertToDTO(updatedCategory);
        }
        return null;
    }

    public boolean deleteCategory(Long id) {
        if (templateCategoryRepository.existsById(id)) {
            templateCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TemplateCategoryDTO convertToDTO(TemplateCategory templateCategory) {
        return new TemplateCategoryDTO(templateCategory.getId(), templateCategory.getName(), templateCategory.getDescription());
    }

    private TemplateCategory convertToEntity(TemplateCategoryDTO templateCategoryDTO) {
        return new TemplateCategory(templateCategoryDTO.getId(), templateCategoryDTO.getName(), templateCategoryDTO.getDescription());
    }
}
