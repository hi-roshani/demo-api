package com.wagh.demo.api.dto.webhook;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity     @Getter     @Setter
@Table(name = "message_template")
public class MessageTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String templateName;
    private String templateBody;
    private LocalDateTime createdAt;

}