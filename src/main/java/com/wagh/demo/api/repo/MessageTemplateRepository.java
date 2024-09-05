package com.wagh.demo.api.repo;

import com.wagh.demo.api.model.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
}