package com.example.clientprocessing.service;

import com.example.clientprocessing.kafka.producer.ClientCreditProductProducer;
import com.example.clientprocessing.kafka.producer.ClientProductProducer;
import com.example.clientprocessing.model.ClientProduct;
import com.example.clientprocessing.model.Product;
import com.example.clientprocessing.repository.ClientProductRepository;
import com.example.clientprocessing.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientProductService {

    private final ClientProductRepository repository;
    private final ProductRepository productRepository;
    private final ClientProductProducer producer;
    private final ClientCreditProductProducer clientCreditProductProducer;
    @Transactional
    public ClientProduct addClientProduct(String clientId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setClientId(Long.valueOf(clientId));
        clientProduct.setProductId(Long.valueOf(product.getProductId()));
        clientProduct.setOpenDate(LocalDate.now().atStartOfDay());
        clientProduct.setStatus(ClientProduct.Status.ACTIVE);

        ClientProduct saved = repository.save(clientProduct);

        producer.sendToTopic(saved);

        return saved;
    }

    public List<ClientProduct> getClientProducts(String clientId) {
        return repository.findByClientId(Long.valueOf(clientId));
    }

    public void closeClientProduct(Long id) {
        ClientProduct clientProduct = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientProduct not found"));
        clientProduct.setStatus(ClientProduct.Status.CLOSED);
        clientProduct.setCloseDate(LocalDate.now().atStartOfDay());
        repository.save(clientProduct);
    }

    public ClientProduct addClientProduct(ClientProduct clientProduct) {
        ClientProduct saved = repository.save(clientProduct);
        Optional<Product> product =  productRepository.findById(saved.getProductId());
        Product product1 = product.get();
        switch (product1.getKey()) {
            case "DC":
            case "CC":
            case "NS":
            case "PENS":
                producer.sendToTopic(saved);
                break;

            case "IPO":
            case "PC":
            case "AC":
                clientCreditProductProducer.sendToTopic(saved);
                break;

            default:
                System.err.println("⚠️ Неизвестный тип продукта: " + product1.getKey());
        }

        return saved;
    }

}
