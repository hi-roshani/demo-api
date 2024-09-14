package com.wagh.demo.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter     @Setter
public class InteractiveMessageDTO {
    @JsonProperty("type")
    private String type;

    @JsonProperty("button_reply")
    private ButtonReplyDTO buttonReply;

    @JsonProperty("list_reply")  // Add this line
    private ListReplyDTO listReply;
}
