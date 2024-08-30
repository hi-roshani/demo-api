package com.wagh.demo.api.service;

import com.wagh.demo.api.model.Category;
import com.wagh.demo.api.model.TemplateCategory;
import com.wagh.demo.api.repo.CategoryRepository;
import com.wagh.demo.api.repo.TemplateCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateCategoryService {
    @Autowired
    private TemplateCategoryRepository templateCategoryRepository;

    public List<TemplateCategory> findAllCategories() {
        return templateCategoryRepository.findAll();
    }

    public Optional<TemplateCategory> findCategoryById(Long id) {
        return templateCategoryRepository.findById(id);
    }

    public TemplateCategory addCategory(TemplateCategory templateCategory) {
        return templateCategoryRepository.save(templateCategory);
    }

    public TemplateCategory updateCategory(Long id, TemplateCategory templateCategory) {
        if (templateCategoryRepository.existsById(id)) {
            templateCategory.setId(id);
            return templateCategoryRepository.save(templateCategory);
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
}