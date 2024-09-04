package com.wagh.demo.api.control;

import com.wagh.demo.api.dto.webhook.UserFlowDTO;
import com.wagh.demo.api.model.UserFlow;
import com.wagh.demo.api.service.UserFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-flows")
public class UserFlowController {

    @Autowired
    private UserFlowService userFlowService;

    @PostMapping
    public ResponseEntity<UserFlowDTO> createUserFlow(@RequestBody UserFlowDTO userFlowDTO) {
        UserFlow userFlow = convertToEntity(userFlowDTO);
        UserFlow createdUserFlow = userFlowService.createUserFlow(userFlow);
        UserFlowDTO createdUserFlowDTO = convertToDTO(createdUserFlow);
        return new ResponseEntity<>(createdUserFlowDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFlowDTO> getUserFlowById(@PathVariable Long id) {
        Optional<UserFlow> userFlow = userFlowService.getUserFlowById(id);
        return userFlow.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserFlowDTO>> getAllUserFlows() {
        List<UserFlow> userFlows = userFlowService.getAllUserFlows();
        List<UserFlowDTO> userFlowDTOs = userFlows.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(userFlowDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserFlowDTO> updateUserFlow(@PathVariable Long id, @RequestBody UserFlowDTO userFlowDTO) {
        UserFlow userFlow = convertToEntity(userFlowDTO);
        userFlow.setId(id);
        UserFlow updatedUserFlow = userFlowService.updateUserFlow(id, userFlow);
        return updatedUserFlow != null ? ResponseEntity.ok(convertToDTO(updatedUserFlow)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserFlow(@PathVariable Long id) {
        userFlowService.deleteUserFlow(id);
        return ResponseEntity.noContent().build();
    }

    private UserFlowDTO convertToDTO(UserFlow userFlow) {
        UserFlowDTO dto = new UserFlowDTO();
        dto.setId(userFlow.getId());
        dto.setUserId(userFlow.getUserId());
        dto.setUserPhone(userFlow.getUserPhone());
        dto.setTemplateId(userFlow.getTemplateId());
        dto.setBtn1Text(userFlow.getBtn1Text());
        dto.setBtn1Redirect(userFlow.getBtn1Redirect());
        dto.setBtn2Text(userFlow.getBtn2Text());
        dto.setBtn2Redirect(userFlow.getBtn2Redirect());
        dto.setBtn3Text(userFlow.getBtn3Text());
        dto.setBtn3Redirect(userFlow.getBtn3Redirect());
        dto.setListItems(Arrays.asList(userFlow.getListItemsAsArray()));
        return dto;
    }

    private UserFlow convertToEntity(UserFlowDTO userFlowDTO) {
        UserFlow userFlow = new UserFlow();
        userFlow.setId(userFlowDTO.getId());
        userFlow.setUserId(userFlowDTO.getUserId());
        userFlow.setUserPhone(userFlowDTO.getUserPhone());
        userFlow.setTemplateId(userFlowDTO.getTemplateId());
        userFlow.setBtn1Text(userFlowDTO.getBtn1Text());
        userFlow.setBtn1Redirect(userFlowDTO.getBtn1Redirect());
        userFlow.setBtn2Text(userFlowDTO.getBtn2Text());
        userFlow.setBtn2Redirect(userFlowDTO.getBtn2Redirect());
        userFlow.setBtn3Text(userFlowDTO.getBtn3Text());
        userFlow.setBtn3Redirect(userFlowDTO.getBtn3Redirect());
        userFlow.setListItemsFromArray(userFlowDTO.getListItems().toArray(new String[0]));
        return userFlow;
    }
}
