package com.wagh.demo.api.control;

import com.wagh.demo.api.dto.webhook.SendMessageRequest;
import com.wagh.demo.api.service.WhatsappRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageRequestController {

    private final WhatsappRequestService whatsappRequestService;

    @Autowired
    public MessageRequestController(WhatsappRequestService whatsappRequestService) {
        this.whatsappRequestService = whatsappRequestService;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody SendMessageRequest sendMessageRequest) throws Exception {
        // Call the service method with the templateId
        whatsappRequestService.sendMessage(sendMessageRequest.getId());
    }
}

