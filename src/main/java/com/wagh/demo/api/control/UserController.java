package com.wagh.demo.api.control;

import com.wagh.demo.api.dto.webhook.UserDTO;
import com.wagh.demo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<UserDTO> users = userService.searchUsers(query);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Name not found");
        } else {
            return ResponseEntity.ok(users);
        }
    }
}
