package com.wagh.demo.api.control;


import com.wagh.demo.api.dto.webhook.TemplateCategoryDTO;
import com.wagh.demo.api.service.TemplateCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/templates/categories")
public class TemplateCategoryController {

    @Autowired
    private TemplateCategoryService templateCategoryService;

    @GetMapping
    public ResponseEntity<List<TemplateCategoryDTO>> getAllCategories() {
        List<TemplateCategoryDTO> templateCategories = templateCategoryService.findAllCategories();
        return ResponseEntity.ok(templateCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateCategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<TemplateCategoryDTO> templateCategory = templateCategoryService.findCategoryById(id);
        return templateCategory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TemplateCategoryDTO> createCategory(@RequestBody TemplateCategoryDTO templateCategoryDTO) {
        TemplateCategoryDTO createdCategory = templateCategoryService.addCategory(templateCategoryDTO);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateCategoryDTO> updateCategory(@PathVariable Long id, @RequestBody TemplateCategoryDTO templateCategoryDTO) {
        TemplateCategoryDTO updatedCategory = templateCategoryService.updateCategory(id, templateCategoryDTO);
        return updatedCategory != null ? ResponseEntity.ok(updatedCategory)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return templateCategoryService.deleteCategory(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
