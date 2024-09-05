package com.wagh.demo.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wagh.demo.api.model.MessageTemplate;
import com.wagh.demo.api.repo.MessageTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
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

    public void sendMessage(String templateId) throws Exception {
        if (templateId == null || templateId.isEmpty()) {
            log.error("Invalid template ID: {}", templateId);
            throw new IllegalArgumentException("Template ID must not be null or empty");
        }

        MessageTemplate template = messageTemplateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String recipientNumber = "+919049534396";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("messaging_product", "whatsapp");
        rootNode.put("to", recipientNumber);
        rootNode.put("type", "interactive");

        ObjectNode interactiveNode = objectMapper.createObjectNode();
        interactiveNode.put("type", "button");

        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.put("text", template.getTemplateBody());
        interactiveNode.set("body", bodyNode);

        ObjectNode actionNode = objectMapper.createObjectNode();
        ArrayNode buttonsArray = objectMapper.createArrayNode();
        if (template.getButton1Id() != null && template.getButton1() != null) {
            buttonsArray.add(createButtonNode(template.getButton1Id(), template.getButton1()));
        }
        if (template.getButton2Id() != null && template.getButton2() != null) {
            buttonsArray.add(createButtonNode(template.getButton2Id(), template.getButton2()));
        }
        if (template.getButton3Id() != null && template.getButton3() != null) {
            buttonsArray.add(createButtonNode(template.getButton3Id(), template.getButton3()));
        }
        actionNode.set("buttons", buttonsArray);
        interactiveNode.set("action", actionNode);
        rootNode.set("interactive", interactiveNode);

        String payload = objectMapper.writeValueAsString(rootNode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send message: " + response.getBody());
        }
    }

    private ObjectNode createButtonNode(String id, String title) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode buttonNode = objectMapper.createObjectNode();
        buttonNode.put("type", "reply");
        ObjectNode replyNode = objectMapper.createObjectNode();
        replyNode.put("id", id);
        replyNode.put("title", title);
        buttonNode.set("reply", replyNode);
        return buttonNode;
    }


}