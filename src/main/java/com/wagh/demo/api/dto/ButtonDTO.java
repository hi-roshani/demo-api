package com.wagh.demo.api.dto;

import lombok.*;

@AllArgsConstructor     @NoArgsConstructor
@Data
public class ButtonDTO {
   /* private String type; // "reply" or "url"
    private String title;
    private String payload; // For reply buttons
    private String url;*/
    private String id;
    private String title;
    private String type;
    private String url;  // Only for URL buttons
    private String phoneNumber;

 public ButtonDTO(String id, String title, String type) {
  this.id = id;
  this.title = title;
  this.type = type;
 }
}
