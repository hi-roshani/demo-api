package com.wagh.demo.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.HashMap;
import java.util.Map;


@Data
@Entity
@Table(name = "whatsapp_message_templates")
public class MessageTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id", columnDefinition = "uuid")
    private String templateId;


    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "message_body", nullable = false, columnDefinition = "TEXT")
    private String messageBody;

    @Column(name = "message_type", nullable = false)
    private String type;

    @Column(name = "components", columnDefinition = "jsonb")
    private String components;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void setComponents(Map<String, Object> data) {
        try {
            this.components = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Map to JSON", e);
        }
    }

    public Map<String, Object> getComponents() {
        if (this.components != null) {
            try {
                return objectMapper.readValue(this.components, new TypeReference<Map<String, Object>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert JSON to Map", e);
            }
        }
        return new HashMap<>();
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
