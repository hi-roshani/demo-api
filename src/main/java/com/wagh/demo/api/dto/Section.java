package com.wagh.demo.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class Section {
    private String title;
    private List<Row> rows;
}
