package com.wagh.demo.api.dto.webhook;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter     @Setter     @Data
public class ContextDTO {
    private String from;
    private String id;
}
