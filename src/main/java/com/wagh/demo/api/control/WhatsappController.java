package com.wagh.demo.api.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagh.demo.api.service.WhatsappSendMessageService;
import com.wagh.demo.api.service.WhatsappWebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*@Slf4j
@RestController
@RequestMapping("/api/v1")*/
public class WhatsappController
{
/*
    @Value("${whatsapp.mode}")
    private String whatsappMode;

    @Value("${whatsapp.verification_token}")
    private String whatsappVerificationToken;

    @Autowired
    private WhatsappWebhookService whatsappWebhookService;

    @Autowired
    private WhatsappSendMessageService whatsappSendMessageService;


    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/webhook")
    public ResponseEntity<String> webhookVerify(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.challenge") String challenge,
                                                @RequestParam("hub.verify_token") String token) {
        if (mode.equals(whatsappMode) && token.equals(whatsappVerificationToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).body("Verification token or mode mismatch");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody String messageJson) {
        String response = whatsappWebhookService.processWebhookMessage(messageJson);
        return ResponseEntity.ok(response);
    }*/
}