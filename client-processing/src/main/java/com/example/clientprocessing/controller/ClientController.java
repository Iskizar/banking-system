package com.example.clientprocessing.controller;

import com.example.clientprocessing.DTO.RegisterClientRequest;
import com.example.clientprocessing.model.Client;
import com.example.clientprocessing.model.User;
import com.example.clientprocessing.service.BlacklistService;
import com.example.clientprocessing.service.ClientService;
import com.example.clientprocessing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final BlacklistService blacklistService;
    private final ClientService clientService;
    private final UserService userService;

    public ClientController(BlacklistService blacklistService,
                            ClientService clientService,
                            UserService userService) {
        this.blacklistService = blacklistService;
        this.clientService = clientService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody RegisterClientRequest request) {
        Client client = request.getClient();
        String login = request.getLogin();
        String password = request.getPassword();
        String email = request.getEmail();

        if (blacklistService.isBlacklisted(client.getDocumentId())) {
            return ResponseEntity.badRequest().body("Client is in blacklist");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setEmail(email);
        User savedUser = userService.createUser(user);

        client.setUserId(savedUser.getId());
        Client savedClient = clientService.createClient(client);

        return ResponseEntity.ok("User " + savedUser.getLogin() + " registered with clientId: " + savedClient.getClientId());
    }

}
