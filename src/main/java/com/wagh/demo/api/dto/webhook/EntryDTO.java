package com.wagh.demo.api.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter     @Setter
public class EntryDTO {
    private String id;
    private List<ChangeDTO> changes;

}
