package com.wagh.demo.api.dto.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFlowDTO {

    private Long id;
    private Long userId;
    private String userPhone;
    private Long templateId;
    private String btn1Text;
    private String btn1Redirect;
    private String btn2Text;
    private String btn2Redirect;
    private String btn3Text;
    private String btn3Redirect;
    private List<String> listItems;

    // Convert comma-separated string to list of items
    public void setListItemsFromArray(String[] items) {
        this.listItems = items == null ? List.of() : Arrays.asList(items);
    }

    // Convert list of items to comma-separated string
    public String[] getListItemsAsArray() {
        return this.listItems == null ? new String[0] : this.listItems.toArray(new String[0]);
    }
}
