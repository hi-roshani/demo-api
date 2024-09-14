package com.wagh.demo.api.repo;

import com.wagh.demo.api.model.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    /*@Query("SELECT t FROM MessageTemplate t WHERE t.templateId = :templateId")
    Optional<MessageTemplate> findByTemplateId(@Param("templateId") String templateId);

    @Query("SELECT t FROM MessageTemplate t WHERE t.templateId = :buttonId")
    Optional<MessageTemplate> findTemplateByTemplateId(@Param("buttonId") String buttonId);*/

    @Query("SELECT t FROM MessageTemplate t WHERE t.templateName =:templateName")
    Optional<MessageTemplate> findByTemplateName(@Param("templateName") String templateName);

    @Query("SELECT t FROM MessageTemplate t WHERE t.templateName =:buttonId")
    Optional<MessageTemplate> findTemplateByButtonId(@Param("buttonId") String buttonId);

}