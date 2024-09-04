package com.wagh.demo.api.service;

import com.wagh.demo.api.dto.webhook.MessageTemplate;
import com.wagh.demo.api.repo.MessageTemplateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappRequestService {

    @Value("${whatsapp.api.url}")
    private String apiUrl; // Endpoint URL from application.properties

    @Value("${whatsapp.api.token}")
    private String apiToken; // Authorization token from application.properties

    private final RestTemplate restTemplate;
    private final MessageTemplateRepository messageTemplateRepository;

    public WhatsappRequestService(RestTemplate restTemplate, MessageTemplateRepository messageTemplateRepository) {
        this.restTemplate = restTemplate;
        this.messageTemplateRepository = messageTemplateRepository;
    }

    public void sendMessage(Long templateId) {

        MessageTemplate template = messageTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String recipientNumber = "+919049534396";

        // Build request payload with required parameters
        String payload = String.format(
                "{ \"messaging_product\": \"whatsapp\", \"to\": \"%s\", \"type\": \"text\", \"text\": { \"body\": \"%s\" } }",
                recipientNumber,
                template.getTemplateBody()
        );

        // Create headers with authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        // Create HttpEntity with payload and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        // Send POST request to WhatsApp API
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send message: " + response.getBody());
        }
    }

}