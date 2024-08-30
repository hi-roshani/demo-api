package com.wagh.demo.api.control;

import com.wagh.demo.api.model.Category;
import com.wagh.demo.api.model.TemplateCategory;
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
    public ResponseEntity<List<TemplateCategory>> getAllCategories() {
        List<TemplateCategory> templateCategories = templateCategoryService.findAllCategories();
        return ResponseEntity.ok(templateCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateCategory> getCategoryById(@PathVariable Long id) {
        Optional<TemplateCategory> templateCategory = templateCategoryService.findCategoryById(id);
        return templateCategory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TemplateCategory> createCategory(@RequestBody TemplateCategory templateCategory) {
        TemplateCategory createdCategory = templateCategoryService.addCategory(templateCategory);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateCategory> updateCategory(@PathVariable Long id, @RequestBody TemplateCategory templateCategory) {
        TemplateCategory updatedCategory = templateCategoryService.updateCategory(id, templateCategory);
        return updatedCategory != null ? ResponseEntity.ok(updatedCategory)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return templateCategoryService.deleteCategory(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}