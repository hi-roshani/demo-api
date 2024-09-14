package com.wagh.demo.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wagh.demo.api.dto.ButtonDTO;
import com.wagh.demo.api.dto.RowDTO;
import com.wagh.demo.api.dto.SectionDTO;
import com.wagh.demo.api.dto.TemplateDTO;
import com.wagh.demo.api.model.MessageTemplate;
import com.wagh.demo.api.repo.MessageTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WhatsappRequestService {

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate;
    private final MessageTemplateRepository messageTemplateRepository;

    public WhatsappRequestService(RestTemplate restTemplate, MessageTemplateRepository messageTemplateRepository) {
        this.restTemplate = restTemplate;
        this.messageTemplateRepository = messageTemplateRepository;
    }

    String recipientNumber = "+919049534396";

    public void sendMessage(String templateName) {
        try {
            // Fetch the message template from repository
            MessageTemplate template = messageTemplateRepository.findByTemplateName(templateName)
                    .orElseThrow(() -> new RuntimeException("Template not found with id: " + templateName));

            // Convert template to DTO
            TemplateDTO templateDTO = convertToDTO(template);
            log.info("Converted TemplateDTO: {}", templateDTO);

            // Build the message payload
            String messagePayload = buildWhatsAppMessage(templateDTO, recipientNumber);
            log.info("Message Payload: {}", messagePayload);

            // Send message to WhatsApp API
            sendMessageToWhatsAppAPI(messagePayload);

        } catch (Exception e) {
            log.error("Error occurred while sending WhatsApp message", e);
        }
    }

    private TemplateDTO convertToDTO(MessageTemplate template) {
        TemplateDTO dto = new TemplateDTO();
        dto.setType(template.getType());

        // Set the template body directly from the messageBody (assuming it's plain text)
        dto.setTemplateBody(template.getMessageBody());

        // Initialize ObjectMapper to handle JSON parsing for header, footer, buttons, and list sections
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            // Convert components (which is a Map) to a JSON string before parsing
            String componentsJson = objectMapper.writeValueAsString(template.getComponents());
            rootNode = objectMapper.readTree(componentsJson); // Now read as a tree from JSON string
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse components JSON", e);
        }

        // Set common fields (header and footer)
        JsonNode headerNode = rootNode.path("header");
        dto.setHeader(headerNode.isMissingNode() ? null : headerNode.path("text").asText(null));

        JsonNode footerNode = rootNode.path("footer");
        dto.setFooter(footerNode.isMissingNode() ? null : footerNode.path("text").asText(null));

        // Handle different types
        if ("list".equals(template.getType())) {
            JsonNode actionNode = rootNode.path("action");
            dto.setListButtonText(actionNode.path("button").asText(null));

            List<SectionDTO> sections = new ArrayList<>();
            JsonNode sectionsNode = actionNode.path("sections");
            if (sectionsNode.isArray()) {
                for (JsonNode sectionNode : sectionsNode) {
                    String title = sectionNode.path("title").asText(null);
                    List<RowDTO> rows = new ArrayList<>();
                    JsonNode rowsNode = sectionNode.path("rows");
                    if (rowsNode.isArray()) {
                        for (JsonNode rowNode : rowsNode) {
                            rows.add(new RowDTO(
                                    rowNode.path("id").asText(null),
                                    rowNode.path("title").asText(null),
                                    rowNode.path("description").asText(null)
                            ));
                        }
                    }
                    sections.add(new SectionDTO(title, rows));
                }
            }
            dto.setSections(sections);
        } else if ("button".equals(template.getType())) {
            JsonNode actionNode = rootNode.path("action");
            List<ButtonDTO> buttons = new ArrayList<>();
            JsonNode buttonsNode = actionNode.path("buttons");
            if (buttonsNode.isArray()) {
                for (JsonNode buttonNode : buttonsNode) {
                    ButtonDTO buttonDTO = new ButtonDTO(
                            buttonNode.path("id").asText(null),
                            buttonNode.path("title").asText(null),
                            buttonNode.path("type").asText(null)
                    );

                    if ("url".equals(buttonDTO.getType())) {
                        // Set URL if button type is 'url'
                        buttonDTO.setUrl(buttonNode.path("url").asText(null));
                    } else if ("call".equals(buttonDTO.getType())) {
                        // Set phone number if button type is 'call'
                        buttonDTO.setPhoneNumber(buttonNode.path("phone_number").asText(null));
                    }

                    buttons.add(buttonDTO);
                }
            }
            dto.setButtons(buttons);
        }

        // Ensure that templateBody is set (if it's empty, we provide a default value)
        if (dto.getTemplateBody() == null || dto.getTemplateBody().isEmpty()) {
            dto.setTemplateBody("Default message body text");
        }

        return dto;
    }



    private void sendMessageToWhatsAppAPI(String messagePayload) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken.trim());
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(messagePayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to send message: " + response.getBody());
            }
            log.info("Message sent successfully: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error response body: {}", e.getResponseBodyAsString());
            log.error("HTTP Status Code: {}", e.getStatusCode());
            log.error("Error Message: {}", e.getMessage());// Log response body
            throw e; // Re-throw or handle exception as needed
        }  catch (Exception e) {
            log.error("Unexpected error occurred while sending WhatsApp message", e);
            throw e; // Re-throw or handle exception as needed
        }


    }

    public String buildWhatsAppMessage(TemplateDTO template, String recipientNumber) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        // Basic WhatsApp message structure
        rootNode.put("messaging_product", "whatsapp");
        rootNode.put("to", recipientNumber);
        rootNode.put("type", "interactive");

        ObjectNode interactiveNode = objectMapper.createObjectNode();
        interactiveNode.put("type", template.getType());

        // Set body and optional header/footer
        setMessageBody(interactiveNode, template);

        // Build action based on type (button or list)
        if ("button".equals(template.getType())) {
            interactiveNode.set("action", buildButtonAction(objectMapper, template));
        } else if ("list".equals(template.getType())) {
            interactiveNode.set("action", buildListAction(objectMapper, template));
        }

        rootNode.set("interactive", interactiveNode);
        return objectMapper.writeValueAsString(rootNode);
    }

    private void setMessageBody(ObjectNode interactiveNode, TemplateDTO template) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.put("text", template.getTemplateBody() != null ? template.getTemplateBody() : "");
        interactiveNode.set("body", bodyNode);

        // Optional header
        if (template.getHeader() != null) {
            ObjectNode headerNode = objectMapper.createObjectNode();
            headerNode.put("type", "text");
            headerNode.put("text", template.getHeader());
            interactiveNode.set("header", headerNode);
        }

        // Optional footer
        if (template.getFooter() != null) {
            ObjectNode footerNode = objectMapper.createObjectNode();
            footerNode.put("text", template.getFooter());
            interactiveNode.set("footer", footerNode);
        }
    }

    private ObjectNode buildButtonAction(ObjectMapper objectMapper, TemplateDTO template) {
        ObjectNode actionNode = objectMapper.createObjectNode();
        ArrayNode buttonsArray = objectMapper.createArrayNode();

        // Add buttons from the list in TemplateDTO
        if (template.getButtons() != null) {
            for (ButtonDTO button : template.getButtons()) {
                buttonsArray.add(createButtonNode(button.getId(), button.getTitle()));
            }
        }

        // Ensure at least 1 button
        if (buttonsArray.size() == 0) {
            throw new IllegalArgumentException("At least one button must be provided.");
        }

        actionNode.set("buttons", buttonsArray);
        return actionNode;
    }




    private ObjectNode buildListAction(ObjectMapper objectMapper, TemplateDTO template) {
        ObjectNode actionNode = objectMapper.createObjectNode();
        actionNode.put("button", template.getListButtonText() != null ? template.getListButtonText() : "Choose an option");

        ArrayNode sectionsArray = objectMapper.createArrayNode();
        if (template.getSections() != null) {
            for (SectionDTO section : template.getSections()) {
                ObjectNode sectionNode = objectMapper.createObjectNode();
                sectionNode.put("title", section.getTitle());

                ArrayNode rowsArray = objectMapper.createArrayNode();
                for (RowDTO row : section.getRows()) {
                    ObjectNode rowNode = objectMapper.createObjectNode();
                    rowNode.put("id", row.getId());
                    rowNode.put("title", row.getTitle());
                    if (row.getDescription() != null) {
                        rowNode.put("description", row.getDescription());
                    }
                    rowsArray.add(rowNode);
                }
                sectionNode.set("rows", rowsArray);
                sectionsArray.add(sectionNode);
            }
        }
        actionNode.set("sections", sectionsArray);
        return actionNode;
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
