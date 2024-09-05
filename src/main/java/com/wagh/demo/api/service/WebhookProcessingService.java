package com.wagh.demo.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagh.demo.api.dto.webhook.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class WebhookProcessingService {

    private final WhatsappRequestService whatsappRequestService;
    private final ObjectMapper objectMapper;


    public WebhookProcessingService(WhatsappRequestService whatsappRequestService, ObjectMapper objectMapper) {
        this.whatsappRequestService = whatsappRequestService;
        this.objectMapper = objectMapper;
    }


    public void processWebhookResponse(String responseJson) throws Exception {
        log.info("Received Webhook Response: {}", responseJson);

        ObjectMapper objectMapper = new ObjectMapper();
        WebhookResponseDTO webhookResponseDTO = objectMapper.readValue(responseJson, WebhookResponseDTO.class);

        log.info("Webhook response received: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(webhookResponseDTO));

        processEntryDTOs(webhookResponseDTO.getEntry());
    }

    private void processEntryDTOs(List<EntryDTO> entryDTOList) throws Exception {
        if (entryDTOList.isEmpty()) {
            log.info("No entries to process.");
            return;
        }

        log.info("Processing {} entries", entryDTOList.size());

        EntryDTO entryDTO = entryDTOList.get(0);

        if (Boolean.TRUE.equals(hasStatuses(entryDTO))) {
            log.info("Status received");
            return;
        }
        processMessage(entryDTO);
    }

    private Boolean hasStatuses(EntryDTO entryDTO) {
        boolean hasStatuses = entryDTO.getChanges().stream()
                .anyMatch(change -> change.getValue().getStatuses() != null && !change.getValue().getStatuses().isEmpty());

        log.info("Has statuses: {}", hasStatuses);
        return hasStatuses;
    }

    private void processMessage(EntryDTO entryDTO) throws Exception {
        if (entryDTO.getChanges().isEmpty()) {
            log.info("No changes in entry.");
            return;
        }

        log.info("Entry Changes: {}", entryDTO.getChanges());

        String msgType = entryDTO.getChanges().get(0).getValue().getMessages().get(0).getType();
        log.info("Message Type: {}", msgType);

        if (msgType.equals("text")) { // Check for text message type
            log.info("Text message received");
            sendMessageBasedOnUserInfo(entryDTO);
        } else if (msgType.equals("interactive")) {
            InteractiveMessageDTO interactiveMessageDTO = entryDTO.getChanges().get(0).getValue().getMessages().get(0).getInteractive();
            if (interactiveMessageDTO.getType().equals("list_reply")) {
                log.info("List reply received");
                // Handle list reply
            } else if (interactiveMessageDTO.getType().equals("button_reply")) {
                log.info("Button reply received");
                // Handle button reply
            }
        }
    }

    private void sendMessageBasedOnUserInfo(EntryDTO entryDTO) throws Exception {
        // Extract relevant user information from entryDTO
        // Determine which template to use, for example based on user info or message content
        Long templateId = determineTemplateId(entryDTO);

        log.info("Determined Template ID: {}", templateId);
        whatsappRequestService.sendMessage(templateId);
    }

    private Long determineTemplateId(EntryDTO entryDTO) {
        // Implement logic to determine the appropriate template ID
        // This can be based on message content, user information, etc.
        // For simplicity, returning a hardcoded value
        return 1L; // Replace with actual logic
    }
}
