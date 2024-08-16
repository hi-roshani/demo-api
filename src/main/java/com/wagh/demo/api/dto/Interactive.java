package com.wagh.demo.api.dto;

import lombok.Data;

@Data
public class Interactive {
    private String type;
    private Header header;
    private Body body;
    private Footer footer;
    private Action action;
}
