package com.wagh.demo.api.control;

import com.wagh.demo.api.dto.webhook.MessageTemplate;
import com.wagh.demo.api.dto.webhook.MessageTemplateDTO;
import com.wagh.demo.api.repo.MessageTemplateRepository;
import com.wagh.demo.api.service.WhatsappRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/messages")
public class MessageRequestController {

    private final WhatsappRequestService whatsAppRequestService;
    private final MessageTemplateRepository messageTemplateRepository;

    public MessageRequestController(WhatsappRequestService whatsAppRequestService, MessageTemplateRepository messageTemplateRepository) {
        this.whatsAppRequestService = whatsAppRequestService;
        this.messageTemplateRepository = messageTemplateRepository;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageTemplateDTO messageTemplateDTO) {
        MessageTemplate template = messageTemplateRepository.findById(messageTemplateDTO.getId())
                .orElseThrow(() -> new RuntimeException("Template not found"));
        whatsAppRequestService.sendMessage("recipient-phone-number", template.getTemplateBody());
    }
}
