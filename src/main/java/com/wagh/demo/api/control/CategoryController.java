package com.wagh.demo.api.control;

import com.wagh.demo.api.dto.webhook.CategoryDTO;
import com.wagh.demo.api.model.Category;
import com.wagh.demo.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/business/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findCategoryById(id);
        return category.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category createdCategory = categoryService.addCategory(category);
        CategoryDTO createdCategoryDTO = convertToDTO(createdCategory);
        return ResponseEntity.ok(createdCategoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        category.setId(id);
        Category updatedCategory = categoryService.updateCategory(id, category);
        return updatedCategory != null ? ResponseEntity.ok(convertToDTO(updatedCategory))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }
}
