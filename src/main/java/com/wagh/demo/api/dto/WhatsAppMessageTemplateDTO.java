package com.wagh.demo.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter     @Setter     @Data
public class WhatsAppMessageTemplateDTO {
    private Long id;
    private String templateName;
    private String messageBody;
    private String messageType; // "button" or "list"
    private ListDTO list;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
