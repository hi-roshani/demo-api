package com.wagh.demo.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class WhatsappSendMessageService {

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private AtomicBoolean messageSent = new AtomicBoolean(false);


    public void sendWelcomeMessage() {
        if(messageSent.get()){
            log.info("welcome message already sent. Skipping");
            return;
        }
        sendMessageFromFile("list.json");
        messageSent.set(true);
    }

    public void sendDeluxeRoomsDetails(String messageJson) {
        sendMessageFromFile("deluxe.json");
    }

    public void sendDeluxeRoomFeatures(String messageJson) { sendMessageFromFile("deluxeRoomFeatures.json");}

    public void sendDeluxeRoomPricing(String messageJson) { sendMessageFromFile("deluxeRoomPricing.json");}

    public void sendBookDeluxeRoom(String messageJson) { sendMessageFromFile("bookDeluxeRoom.json");}



    public void sendSuiteRoomsDetails(String messageJson) {
        sendMessageFromFile("suite.json");
    }

    public void sendSuiteAmenities(String messageJson) { sendMessageFromFile("suiteAmenities.json");}

    public void sendSuiteSpecialOffers(String messageJson) { sendMessageFromFile("suiteSpecialOffers.json");}

    public void sendSuitePersonalizedServices(String messageJson) { sendMessageFromFile("suitePersonalizedServices.json");}

    public void sendSuiteGuestExperiences(String messageJson) { sendMessageFromFile("suiteGuestExperiences.json");}



    public void sendStandardRoomsDetails(String messageJson) { sendMessageFromFile("standard.json");}

    public void sendStandardRoomFeatures(String messageJson) { sendMessageFromFile("standardRoomFeatures.json");}

    public void sendStandardRoomAmenityList(String messageJson) { sendMessageFromFile(".json");}

    public void sendStandardRoomCustomisation(String messageJson) { sendMessageFromFile("");}



    public void sendSpecialPackagesDetails(String messageJson) {
        sendMessageFromFile("special.json");
    }

    public void sendSpecialPackageHighlights(String messageJson) {
        sendMessageFromFile("");
    }

    public void sendSeasonalDeals(String messageJson) {
        sendMessageFromFile("");
    }

    public void sendAddOnServices(String messageJson) {
        sendMessageFromFile("");
    }

    public void sendPackageReviews(String messageJson) {
        sendMessageFromFile("");
    }



    public void sendSwimmingPoolDetails(String messageJson) {
        sendMessageFromFile("pool.json");
    }

    public void sendSpaDetails(String messageJson) {
        sendMessageFromFile("spa.json");
    }

    public void sendGymDetails(String messageJson) {
        sendMessageFromFile("gym.json");
    }

    public void sendRoomServiceDetails(String messageJson) {
        sendMessageFromFile("roomService.json");
    }

    public void sendHouseKeepingDetails(String messageJson) {
        sendMessageFromFile("housekeeping.json");
    }

    public void sendGuestAssistanceDetails(String messageJson) {
        sendMessageFromFile("guestAssistance.json");
    }



    public void sendMessageToPhoneNumber(String phoneNumber, String messageJson) {
        log.info("Sending message to phone number: {}", phoneNumber);
        sendMessage(messageJson);
    }

    public void sendMainMenu(String messageJson){
        sendMessageFromFile("list.json");
    }

    public boolean isWelcomeMessageSent() {
        return messageSent.get();
    }

    private void sendMessage(String messageJson) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = new HttpEntity<>(messageJson, headers);
        try {
            log.info("Sending request to API URL: {}", apiUrl);
            log.info("Request Headers: {}", headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("MessageTable sent successfully: {}", responseEntity.getBody());
            } else {
                log.error("Failed to send message, status code: {}", responseEntity.getStatusCodeValue());
                log.error("Response body: {}", responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error("HTTP error: Status code: {}, Response body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Exception occurred while sending message", e);
        }
    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);
        return headers;
    }

    private String readMessageFromFile(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));
            String content = new String(fileBytes);
            log.debug("Content of {}: {}", fileName, content);
            return content;
        } catch (IOException e) {
            log.error("Error reading message file: {}", fileName, e);
            return null;
        }
    }

    private void sendMessageFromFile(String fileName) {
        String messageJson = readMessageFromFile(fileName);
        if (messageJson != null) {
            log.debug("MessageTable JSON read from file: {}", messageJson);
            log.debug("calling sendMessage method");
            sendMessage(messageJson); // Ensure the actual content is passed
        } else {
            log.error("Failed to read message from file: {}", fileName);
        }
    }
}