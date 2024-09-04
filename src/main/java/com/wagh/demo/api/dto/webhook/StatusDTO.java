package com.wagh.demo.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter     @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusDTO {
    private String id;
    private String status;
    private String timestamp;
    private String recipient_id;
    private ConversationDTO conversation;
    private PricingDTO pricing;
}
