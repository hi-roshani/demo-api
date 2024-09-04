package com.wagh.demo.api.dto.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateCategoryDTO {

    private Long id;
    private String name;
    private String description;
}
