package com.wagh.demo.api.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
@Entity
@Table(name = "user_flow")
public class UserFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "btn1_text")
    private String btn1Text;

    @Column(name = "btn1_redirect")
    private String btn1Redirect;

    @Column(name = "btn2_text")
    private String btn2Text;

    @Column(name = "btn2_redirect")
    private String btn2Redirect;

    @Column(name = "btn3_text")
    private String btn3Text;

    @Column(name = "btn3_redirect")
    private String btn3Redirect;

    @JsonIgnore
    @Column(name = "list_items")
    private String listItems;

    // Convert comma-separated string to array of items
    @JsonProperty("listItems")
    @Transient
    public String[] getListItemsAsArray() {
        if (listItems == null || listItems.isEmpty()) {
            return new String[0];
        }
        return listItems.split(",");
    }

    // Convert array of items to comma-separated string
    @Transient
    public void setListItemsFromArray(String[] items) {
        if (items == null || items.length == 0) {
            this.listItems = "";
        } else {
            this.listItems = Arrays.stream(items).collect(Collectors.joining(","));
        }
    }
}
