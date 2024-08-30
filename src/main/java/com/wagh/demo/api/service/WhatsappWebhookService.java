package com.wagh.demo.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagh.demo.api.model.Message;
import com.wagh.demo.api.repo.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhatsappWebhookService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WhatsappSendMessageService whatsappSendMessageService;

    @Autowired
    private MessageRepository messageRepository;

    public String processWebhookMessage(String messageJson) {
        log.info("Received message JSON: {}", messageJson);

        String messageType = extractMessageType(messageJson);
        log.info("MessageTable type identified: {}", messageType);

        Message message = new Message();

        String sender = extractSender(messageJson, messageType);
        String receiver = extractReceiver(messageJson, messageType);

        message.setSender(sender);
        message.setReceiver(receiver);

        boolean shouldSaveMessage = false;

        switch (messageType) {
            case "interactive_response":
                String selectedOption = extractSelectedOption(messageJson);
                if (selectedOption != null) {
                    log.info("Processing interactive response for option: {}", selectedOption);
                    handleInteractiveResponse(messageJson, selectedOption);
                    shouldSaveMessage = true;

                } else {
                    log.warn("Selected option not found in interactive response");
                }
                break;

            case "message":
                String messageText = extractMessageText(messageJson);
                log.info("Extracted message text: {}", messageText);
                message.setContent(messageText);
                handleInteractiveResponse(messageJson, messageText);
                shouldSaveMessage = true;
                break;

            case "status_update":
                log.info("Received status update: {}", messageJson);
                handleStatusUpdate(messageJson);

                break;

            default:
                log.warn("Unknown message type received: {}", messageType);
                break;
        }

        if (!whatsappSendMessageService.isWelcomeMessageSent()) {
            try {
                whatsappSendMessageService.sendWelcomeMessage();
            } catch (Exception e) {
                log.error("Error sending welcome message", e);
            }
        }
        log.info("Received message JSON: {}", messageJson);

        if (shouldSaveMessage) {
            messageRepository.save(message);
        }

        return "MessageTable received successfully";
    }

    private void handleStatusUpdate(String messageJson) {
        // Extract relevant information from the status update
        try {
            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode statusNode = root.path("entry").get(0).path("changes").get(0).path("value").path("statuses").get(0);

            String statusId = statusNode.path("id").asText();
            String status = statusNode.path("status").asText();
            String recipientId = statusNode.path("recipient_id").asText();
            String timestamp = statusNode.path("timestamp").asText();

            log.info("Status ID: {}, Status: {}, Recipient ID: {}, Timestamp: {}", statusId, status, recipientId, timestamp);

            // Here you could save or process the status update as needed
            // For example, update the status in the database, if applicable
        } catch (Exception e) {
            log.error("Error handling status update", e);
        }
    }

    private String extractMessageType(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode valueNode = root.path("entry").get(0).path("changes").get(0).path("value");

            if (valueNode.has("statuses")) {
                return "status_update";
            } else if (valueNode.has("messages")) {
                return "message";
            } else if (valueNode.has("interactive")) {
                return "interactive_response";
            }
            return "unknown_type";
        } catch (Exception e) {
            log.error("Error parsing message type from JSON", e);
            return "unknown_type";
        }
    }

    private String extractSelectedOption(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");

            JsonNode messagesNode = valueNode.path("messages");

            if (messagesNode.isArray() && !messagesNode.isEmpty()) {
                for (JsonNode messageNode : messagesNode) {
                    JsonNode interactiveNode = messageNode.path("interactive");
                    if (interactiveNode != null && interactiveNode.has("list_reply")) {
                        JsonNode listReplyNode = interactiveNode.path("list_reply");
                        if (listReplyNode.has("id")) {
                            return listReplyNode.path("id").asText();
                        }
                    }
                }
            } else {
                log.warn("Messages node is missing or empty in the JSON");
            }
        } catch (Exception e) {
            log.error("Error extracting selected option from JSON", e);
        }
        return null;
    }

    private String extractSender(String json, String messageType) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");

            if ("message".equals(messageType)) {
                JsonNode messagesNode = valueNode.path("messages");
                if (messagesNode.isArray() && !messagesNode.isEmpty()) {
                    for (JsonNode messageNode : messagesNode) {
                        JsonNode fromNode = messageNode.path("from");
                        if (fromNode.isTextual()) {
                            return fromNode.asText();
                        }
                    }
                }
            } else if ("status_update".equals(messageType)) {
                JsonNode statusesNode = valueNode.path("statuses");
                if (statusesNode.isArray() && !statusesNode.isEmpty()) {
                    JsonNode statusNode = statusesNode.get(0);
                    JsonNode recipientIdNode = statusNode.path("recipient_id");
                    if (recipientIdNode.isTextual()) {
                        return recipientIdNode.asText();
                    }
                }
            }

            return "Sender not found";
        } catch (Exception e) {
            log.error("Error extracting sender from JSON", e);
            return "Sender extraction failed";
        }
    }

    private String extractReceiver(String json, String messageType) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");

            if ("message".equals(messageType)) {
                JsonNode messagesNode = valueNode.path("messages");
                if (messagesNode.isArray() && !messagesNode.isEmpty()) {
                    // Assuming the receiver's phone number can be inferred from context or metadata
                    return "Receiver phone number not explicitly available";
                }
            } else if ("status_update".equals(messageType)) {
                JsonNode statusesNode = valueNode.path("statuses");
                if (statusesNode.isArray() && !statusesNode.isEmpty()) {
                    JsonNode statusNode = statusesNode.get(0);
                    JsonNode recipientIdNode = statusNode.path("recipient_id");
                    if (recipientIdNode.isTextual()) {
                        return recipientIdNode.asText();
                    }
                }
            }

            return "Receiver not found";
        } catch (Exception e) {
            log.error("Error extracting receiver from JSON", e);
            return "Receiver extraction failed";
        }
    }


    private void handleInitialOptions(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "l1":
                whatsappSendMessageService.sendDeluxeRoomsDetails(messageJson);
                break;
            case "l2":
                whatsappSendMessageService.sendSuiteRoomsDetails(messageJson);
                break;
            case "l3":
                whatsappSendMessageService.sendStandardRoomsDetails(messageJson);
                break;
            case "l4":
                whatsappSendMessageService.sendSpecialPackagesDetails(messageJson);
                break;
            case "l5":
                whatsappSendMessageService.sendSwimmingPoolDetails(messageJson);
                break;
            case "l6":
                whatsappSendMessageService.sendSpaDetails(messageJson);
                break;
            case "l7":
                whatsappSendMessageService.sendGymDetails(messageJson);
                break;
            case "l8":
                whatsappSendMessageService.sendRoomServiceDetails(messageJson);
                break;
            case "l9":
                whatsappSendMessageService.sendHouseKeepingDetails(messageJson);
                break;
            case "l0":
                whatsappSendMessageService.sendGuestAssistanceDetails(messageJson);
                break;
            default:
                log.warn("Unknown initial option selected: {}", selectedOption);
        }
    }

    private void handleInteractiveResponse(String messageJson, String selectedOption) {
        log.info("Handling interactive response for option: {}", selectedOption);

        if (selectedOption.equals("go_back")) {
            handleGoBackOption(messageJson);
        } else if (selectedOption.startsWith("deluxe_")) {
            handleDeluxeRoomFollowUps(messageJson, selectedOption);
        } else if (selectedOption.startsWith("suite_")) {
            handleSuiteRoomFollowUps(messageJson, selectedOption);
        } else if (selectedOption.startsWith("standard_")) {
            handleStandardRoomFollowUps(messageJson, selectedOption);
        } else if (selectedOption.startsWith("special_")) {
            handleSpecialPackageFollowUps(messageJson, selectedOption);
        } else {
            handleInitialOptions(messageJson, selectedOption);
        }
    }

    private void handleGoBackOption(String messageJson) {
        whatsappSendMessageService.sendMainMenu(messageJson);
    }


    private void handleDeluxeRoomFollowUps(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "deluxe_room_features":
                whatsappSendMessageService.sendDeluxeRoomFeatures(messageJson);
                break;
            case "deluxe_pricing":
                whatsappSendMessageService.sendDeluxeRoomPricing(messageJson);
                break;
            case "deluxe_book_now":
                whatsappSendMessageService.sendBookDeluxeRoom(messageJson);
                break;
            default:
                log.warn("Unknown follow-up option for Deluxe Rooms: {}", selectedOption);
        }
    }

    private void handleSuiteRoomFollowUps(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "suite_amenities":
                whatsappSendMessageService.sendSuiteAmenities(messageJson);
                break;
            case "suite_special_offers":
                whatsappSendMessageService.sendSuiteSpecialOffers(messageJson);
                break;
            case "suite_personalized_services":
                whatsappSendMessageService.sendSuitePersonalizedServices(messageJson);
                break;
            case "suite_guest_experiences":
                whatsappSendMessageService.sendSuiteGuestExperiences(messageJson);
                break;
            default:
                log.warn("Unknown follow-up option for Suite Rooms: {}", selectedOption);
        }
    }

    private void handleStandardRoomFollowUps(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "standard_room_features":
                whatsappSendMessageService.sendStandardRoomFeatures(messageJson);
                break;
            case "standard_pricing":
                whatsappSendMessageService.sendStandardRoomAmenityList(messageJson);
                break;
            case "standard_book_now":
                whatsappSendMessageService.sendStandardRoomCustomisation(messageJson);
                break;
            case "go_back":
                whatsappSendMessageService.sendMainMenu(messageJson);
                break;
            default:
                log.warn("Unknown follow-up option for Standard Rooms: {}", selectedOption);
        }
    }

    private void handleSpecialPackageFollowUps(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "special_package_highlights":
                whatsappSendMessageService.sendSpecialPackageHighlights(messageJson);
                break;
            case "special_seasonal_deals":
                whatsappSendMessageService.sendSeasonalDeals(messageJson);
                break;
            case "special_add_on_services":
                whatsappSendMessageService.sendAddOnServices(messageJson);
                break;
            case "special_package_reviews":
                whatsappSendMessageService.sendPackageReviews(messageJson);
            default:
                log.warn("Unknown follow-up option for Special Packages: {}", selectedOption);
        }
    }

    private String extractMessageText(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");

            JsonNode messagesNode = valueNode.path("messages");
            if (messagesNode.isArray() && !messagesNode.isEmpty()) {
                for (JsonNode messageNode : messagesNode) {
                    if (messageNode.has("text")) {
                        JsonNode textNode = messageNode.path("text").path("body");
                        if (textNode.isTextual()) {
                            return textNode.asText();
                        }
                    } else if (messageNode.has("interactive")) {
                        JsonNode interactiveNode = messageNode.path("interactive");
                        if (interactiveNode.has("list_reply")) {
                            JsonNode listReplyNode = interactiveNode.path("list_reply");
                            if (listReplyNode.has("id")) {
                                return listReplyNode.path("id").asText();
                            }
                        }
                    }
                }
            } else {
                log.warn("Messages node is missing or empty in the JSON");
            }
        } catch (Exception e) {
            log.error("Error extracting message text from JSON", e);
        }
        return "MessageTable text not found";
    }

    private String extractPhoneNumber(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");
            JsonNode contactsNode = valueNode.path("contacts").get(0);

            if (contactsNode != null && contactsNode.has("wa_id")) {
                return contactsNode.path("wa_id").asText();
            } else {
                return "Phone number not found";
            }
        } catch (Exception e) {
            log.error("Error parsing phone number from JSON", e);
            return null;
        }
    }
}