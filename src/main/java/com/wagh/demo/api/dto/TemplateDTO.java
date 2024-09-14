package com.wagh.demo.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class TemplateDTO {
    private String type; // "button" or "list"
    private String templateBody; // For list and button types
    private String header; // Optional, for header text
    private String footer; // Optional, for footer text
    private String listButtonText; // For list type, button text
    private List<SectionDTO> sections; // For list type, sections and rows
    private List<ButtonDTO> buttons; // For button type, button details
    // Remove button1, button2, button3, button1Id, etc.
}
