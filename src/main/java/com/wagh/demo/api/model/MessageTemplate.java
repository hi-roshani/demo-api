package com.wagh.demo.api.model;

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
    @Column(name = "template_id")
    private String templateId;
    @Column(name = "template_name")
    private String templateName;
    @Column(name = "template_type")
    private String templateType;
    @Column(name = "template_body")
    private String templateBody;
    private String button1;
    @Column(name = "button1_id")
    private String button1Id;
    private String button2;
    @Column(name = "button2_id")
    private String button2Id;
    private String button3;
    @Column(name = "button3_id")
    private String button3Id;
    @Column(name = "list_items")
    private String listItems;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}