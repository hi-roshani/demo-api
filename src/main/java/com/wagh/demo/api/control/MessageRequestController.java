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
    public void sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {
        // Call the service method with the templateId
        whatsappRequestService.sendMessage(sendMessageRequest.getId());
    }
}
/*
@RestController
@RequestMapping("/api/messages")
public class MessageRequestController {

    private final WhatsappRequestService whatsAppRequestService;
    private final MessageTemplateRepository messageTemplateRepository;

    @Autowired
    public MessageRequestController(WhatsappRequestService whatsAppRequestService, MessageTemplateRepository messageTemplateRepository) {
        this.whatsAppRequestService = whatsAppRequestService;
        this.messageTemplateRepository = messageTemplateRepository;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageTemplateDTO messageTemplateDTO) {
        MessageTemplate template = messageTemplateRepository.findById(messageTemplateDTO.getId())
                .orElseThrow(() -> new RuntimeException("Template not found"));
        whatsAppRequestService.sendMessage("+919049534396", template.getTemplateBody());
    }
}
*/
