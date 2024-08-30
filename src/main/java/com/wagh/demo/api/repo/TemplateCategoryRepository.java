package com.wagh.demo.api.repo;

import com.wagh.demo.api.model.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Long> {
}
