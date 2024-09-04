package com.wagh.demo.api.dto.webhook;

import lombok.Getter;
import lombok.Setter;

@Getter     @Setter
public class ChangeDTO {
    private String field;
    private ValueDTO value;
}
