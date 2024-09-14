package com.wagh.demo.api.dto;

import lombok.*;

@AllArgsConstructor     @NoArgsConstructor
@Data
public class RowDTO {
    private String id;
    private String title;
    private String description;
}
