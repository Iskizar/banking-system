package com.example.clientprocessing.service;

import com.example.clientprocessing.config.ClientProperties;
import com.example.clientprocessing.model.Client;
import com.example.clientprocessing.repository.ClientRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    ClientProperties clientProperties = new ClientProperties();
    private final ClientRepository clientRepository;
    private final String REGION_CODE = clientProperties.getRegionCode();
    private final String DEPARTMENT_CODE = clientProperties.getDepartmentCode();

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client createClient(Client client) {
        long nextNumber = clientRepository.count() + 1; // временно так, лучше sequence в БД
        String clientId = REGION_CODE + DEPARTMENT_CODE + String.format("%08d", nextNumber);
        client.setClientId(clientId);

        return clientRepository.save(client);
    }
}
