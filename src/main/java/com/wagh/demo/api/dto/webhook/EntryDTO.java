package com.wagh.demo.api.dto.webhook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter     @Setter
public class EntryDTO {
    private String id;
    private List<ChangeDTO> changes;

}
