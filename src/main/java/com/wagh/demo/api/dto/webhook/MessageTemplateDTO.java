package com.wagh.demo.api.dto.webhook;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter     @Setter     @Data
public class MessageTemplateDTO {

    private Long id;
    private String templateName;
    private String templateBody;
}

