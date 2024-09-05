package com.wagh.demo.api.dto.webhook;

import lombok.Getter;
import lombok.Setter;

@Getter     @Setter
public class MessageDTO {
    private String from;
    private String id;
    private String timestamp;
    private TextDTO text;
    private String type;
    private InteractiveMessageDTO interactive;
    private ContextDTO context;

}
