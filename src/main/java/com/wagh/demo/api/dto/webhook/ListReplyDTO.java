package com.wagh.demo.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListReplyDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;
}
