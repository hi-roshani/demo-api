package com.wagh.demo.api.control;
import com.wagh.demo.api.model.UserFlow;
import com.wagh.demo.api.service.UserFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-flows")
public class UserFlowController {

    @Autowired
    private UserFlowService userFlowService;

    @PostMapping
    public ResponseEntity<UserFlow> createUserFlow(@RequestBody UserFlow userFlow) {
        UserFlow createdUserFlow = userFlowService.createUserFlow(userFlow);
        return new ResponseEntity<>(createdUserFlow, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFlow> getUserFlowById(@PathVariable Long id) {
        Optional<UserFlow> userFlow = userFlowService.getUserFlowById(id);
        return userFlow.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserFlow>> getAllUserFlows() {
        List<UserFlow> userFlows = userFlowService.getAllUserFlows();
        return ResponseEntity.ok(userFlows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserFlow> updateUserFlow(@PathVariable Long id, @RequestBody UserFlow userFlow) {
        UserFlow updatedUserFlow = userFlowService.updateUserFlow(id, userFlow);
        return updatedUserFlow != null ? ResponseEntity.ok(updatedUserFlow) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserFlow(@PathVariable Long id) {
        userFlowService.deleteUserFlow(id);
        return ResponseEntity.noContent().build();
    }
}
