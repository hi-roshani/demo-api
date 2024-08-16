package com.wagh.demo.api.dto;

import lombok.Data;

@Data
public class WhatsappMessageDTO {
    private String messaging_product;
    private String to;
    private String type;
    private Interactive interactive;
}
