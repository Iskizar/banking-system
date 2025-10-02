package com.example.clientprocessing.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProductMessage {
    private String clientId;
    private String key;   // DC, CC, NS, PENS
    private String name;

}
