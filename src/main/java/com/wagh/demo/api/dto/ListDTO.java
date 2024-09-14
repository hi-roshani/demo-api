package com.wagh.demo.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter     @Setter     @Data
public class ListDTO {
    private String header;
    private String body;
    private List<SectionDTO> sections;

}
