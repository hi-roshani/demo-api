package com.wagh.demo.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagh.demo.api.dto.webhook.*;
import com.wagh.demo.api.model.MessageTemplate;
import com.wagh.demo.api.repo.MessageTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WebhookProcessingService {

    private final WhatsappRequestService whatsappRequestService;
    private final ObjectMapper objectMapper;
    private final MessageTemplateRepository messageTemplateRepository;

    public WebhookProcessingService(WhatsappRequestService whatsappRequestService, ObjectMapper objectMapper, MessageTemplateRepository messageTemplateRepository) {
        this.whatsappRequestService = whatsappRequestService;
        this.objectMapper = objectMapper;
        this.messageTemplateRepository = messageTemplateRepository;
    }


    public void processWebhookResponse(String responseJson) throws Exception {
        log.info("Received Webhook Response: {}", responseJson);

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
                handleButtonReply(interactiveMessageDTO);
            }
        }
    }

    private void handleButtonReply(InteractiveMessageDTO interactiveMessageDTO) throws Exception {
        String buttonId = interactiveMessageDTO.getButtonReply().getId();
        log.info("Button ID received: {}", buttonId);

        // Find the template ID corresponding to the button ID
        Optional<MessageTemplate> optionalTemplateId = messageTemplateRepository.findTemplateByTemplateId(buttonId);

        log.info("Query result for Button ID '{}': {}", buttonId, optionalTemplateId);

        if (optionalTemplateId.isPresent()) {
            String templateId = optionalTemplateId.get().getTemplateId();
            log.info("Found Template ID: {}", templateId);
            // Send message using the found template ID
            whatsappRequestService.sendMessage(templateId);
        } else {
            log.warn("No template found for Button ID: {}", buttonId);
        }
    }


    private void sendMessageBasedOnUserInfo(EntryDTO entryDTO) throws Exception {
        // Extract relevant user information from entryDTO
        // Determine which template to use, for example based on user info or message content
        String templateId = determineTemplateId(entryDTO);

        log.info("Determined Template ID: {}", templateId);
        whatsappRequestService.sendMessage(templateId);
    }

    private String determineTemplateId(EntryDTO entryDTO) {
        // Implement logic to determine the appropriate template ID
        // This can be based on message content, user information, etc.
        // For simplicity, returning a hardcoded value
        return "1"; // Replace with actual logic
    }
}
