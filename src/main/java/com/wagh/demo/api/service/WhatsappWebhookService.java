package com.wagh.demo.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhatsappWebhookService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WhatsappSendMessageService whatsappSendMessageService;

    public String processWebhookMessage(String messageJson) {
        log.info("Received message JSON: {}", messageJson);

        String messageType = extractMessageType(messageJson);
        log.info("Message type identified: {}", messageType);

        switch (messageType) {
            case "interactive_response":
                String selectedOption = extractSelectedOption(messageJson);
                if (selectedOption != null) {
                    log.info("Processing interactive response for option: {}", selectedOption);
                    handleInteractiveResponse(messageJson, selectedOption);
                } else {
                    log.warn("Selected option not found in interactive response");
                }
                break;

            case "message":
                String messageText = extractMessageText(messageJson);
                log.info("Extracted message text: {}", messageText);
                handleInteractiveResponse(messageJson, messageText);

                break;

            case "status_update":
                log.info("Received status update: {}", messageJson);
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

        return "Message received successfully";
    }

    private String extractMessageType(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode entryNode = root.path("entry").get(0);
            JsonNode changesNode = entryNode.path("changes").get(0);
            JsonNode valueNode = changesNode.path("value");

            if (valueNode.has("statuses")) {
                JsonNode statusesNode = valueNode.path("statuses");
                if (statusesNode.isArray() && statusesNode.size() > 0) {
                    return "status_update";
                }
            } else if (valueNode.has("messages")) {
                JsonNode messagesNode = valueNode.path("messages");
                if (messagesNode.isArray() && messagesNode.size() > 0) {
                    return "message";
                }
            } else if (valueNode.has("interactive")) {
                return "interactive_response";
            }

            return "unknown_type";
        } catch (Exception e) {
            log.error("Error parsing message type from JSON", e);
            return null;
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

    private void handleInitialOptions(String messageJson, String selectedOption) {
        switch (selectedOption) {
            case "deluxe_rooms":
                whatsappSendMessageService.sendDeluxeRoomsDetails(messageJson);
                break;
            case "suite_rooms":
                whatsappSendMessageService.sendSuiteRoomsDetails(messageJson);
                break;
            case "standard_rooms":
                whatsappSendMessageService.sendStandardRoomsDetails(messageJson);
                break;
            case "special_packages":
                whatsappSendMessageService.sendSpecialPackagesDetails(messageJson);
                break;
            case "pool":
                whatsappSendMessageService.sendSwimmingPoolDetails(messageJson);
                break;
            case "spa":
                whatsappSendMessageService.sendSpaDetails(messageJson);
                break;
            case "gym":
                whatsappSendMessageService.sendGymDetails(messageJson);
                break;
            case "room_service":
                whatsappSendMessageService.sendRoomServiceDetails(messageJson);
                break;
            case "housekeeping":
                whatsappSendMessageService.sendHouseKeepingDetails(messageJson);
                break;
            case "guest_assistance":
                whatsappSendMessageService.sendGuestAssistanceDetails(messageJson);
                break;
            default:
                log.warn("Unknown initial option selected: {}", selectedOption);
        }
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
  /*  private void handleInteractiveResponse(String messageJson, String selectedOption) {

        log.info("Handling interactive response for option: {}", selectedOption);

        switch (selectedOption) {
            case "deluxe_rooms":
                whatsappSendMessageService.sendDeluxeRoomsDetails(messageJson);
                break;
            case "suite_rooms":
                whatsappSendMessageService.sendSuiteRoomsDetails(messageJson);
                break;
            case "standard_rooms":
                whatsappSendMessageService.sendStandardRoomsDetails(messageJson);
                break;
            case "special_packages":
                whatsappSendMessageService.sendSpecialPackagesDetails(messageJson);
                break;
            case "pool":
                whatsappSendMessageService.sendSwimmingPoolDetails(messageJson);
                break;
            case "spa":
                whatsappSendMessageService.sendSpaDetails(messageJson);
                break;
            case "gym":
                whatsappSendMessageService.sendGymDetails(messageJson);
                break;
            case "room_service":
                whatsappSendMessageService.sendRoomServiceDetails(messageJson);
                break;
            case "housekeeping":
                whatsappSendMessageService.sendHouseKeepingDetails(messageJson);
                break;
            case "guest_assistance":
                whatsappSendMessageService.sendGuestAssistanceDetails(messageJson);
                break;
            default:
                log.warn("Unknown option selected: {}", selectedOption);
        }
    }*/

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
                        JsonNode textNode = messageNode.path("text");
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
        return "Message text not found";
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
