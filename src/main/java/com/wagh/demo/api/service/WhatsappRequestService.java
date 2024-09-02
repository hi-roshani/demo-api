package com.wagh.demo.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappRequestService {
    @Value("${whatsapp.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public WhatsappRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String to, String message) {
        // Build request payload
        // You need to format the request according to WhatsApp Business API documentation
        String payload = "{ \"to\": \"" + to + "\", \"message\": \"" + message + "\" }";

        // Send POST request to WhatsApp API
        restTemplate.postForObject(apiUrl, payload, String.class);
    }
}
