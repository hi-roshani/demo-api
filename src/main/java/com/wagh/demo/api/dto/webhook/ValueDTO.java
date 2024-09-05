package com.wagh.demo.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter     @Setter     @Data
public class ValueDTO {
    private String messaging_product;
    private List<MessageDTO> messages;
    private List<StatusDTO> statuses;
    private MetadataDTO metadata;
    private List<ContactDTO> contacts;
}
