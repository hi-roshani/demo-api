package com.wagh.demo.api.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor     @NoArgsConstructor
@Data
public class SectionDTO {
    private String title;
    private List<RowDTO> rows;
}
