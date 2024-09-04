package com.wagh.demo.api.dto.webhook;

import lombok.Getter;
import lombok.Setter;

@Getter     @Setter
public class ConversationDTO {
    private String id;
    private String expiration_timestamp;
    private OriginDTO origin;
}
