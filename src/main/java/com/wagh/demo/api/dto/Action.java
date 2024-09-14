package com.wagh.demo.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class Action {
    private String button;
    private List<SectionDTO> sections;
}
