package com.example.clientprocessing.controller;

import com.example.clientprocessing.model.ClientProduct;
import com.example.clientprocessing.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/client-products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService service;

    @PostMapping
    public ResponseEntity<ClientProduct> addClientProduct(
            @RequestParam("clientId") String clientId,
            @RequestParam("productId") Long productId) {
        return ResponseEntity.ok(service.addClientProduct(clientId, productId));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<List<ClientProduct>> getClientProducts(@PathVariable("clientId") String clientId) {
        return ResponseEntity.ok(service.getClientProducts(clientId));
    }


    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeClientProduct(@PathVariable("id") Long id) {
        service.closeClientProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test-send")
    public ResponseEntity<String> testSendClientProduct(@RequestParam("clientId") Long clientId) {
        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setClientId(clientId);
        clientProduct.setProductId(1L);
        clientProduct.setStatus(ClientProduct.Status.valueOf("ACTIVE"));
        clientProduct.setOpenDate(LocalDate.now().atStartOfDay());
        clientProduct.setCloseDate(LocalDate.now().plusDays(30).atStartOfDay());

        service.addClientProduct(clientProduct);

        return ResponseEntity.ok("ClientProduct отправлен и сохранён");
    }


}
