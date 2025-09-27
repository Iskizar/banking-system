package com.example.clientprocessing.DTO;

import com.example.clientprocessing.model.Client;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterClientRequest {
    private Client client;
    private String login;
    private String password;
    private String email;
}
