package com.wagh.demo.api.dto.webhook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter     @Setter
public class ValueDTO {
    private String messaging_product;
    private List<MessageDTO> messages;
    private List<StatusDTO> statuses;
    private MetadataDTO metadata;
    private List<ContactDTO> contacts;
}
