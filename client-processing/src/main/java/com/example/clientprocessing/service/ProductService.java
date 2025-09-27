package com.example.clientprocessing.service;

import com.example.clientprocessing.model.ClientProduct;
import com.example.clientprocessing.model.Product;
import com.example.clientprocessing.repository.ClientProductRepository;
import com.example.clientprocessing.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ClientProductRepository clientProductRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<Object, ClientProduct> kafkaTemplate;

    private static final String TOPIC = "client_cards";

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public ClientProduct createCard(Long clientId, Long productId) {
        Product product = getProductById(productId);
        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setClientId(clientId);
        clientProduct.setProductId(product.getId());
        clientProduct.setOpenDate(LocalDateTime.now());
        clientProduct.setStatus(ClientProduct.Status.ACTIVE);

        clientProduct = clientProductRepository.save(clientProduct);

        ClientProduct saved = clientProductRepository.save(clientProduct);
        kafkaTemplate.send(TOPIC, saved);

        return saved;

    }
    public ProductService(ClientProductRepository clientProductRepository, ProductRepository productRepository, KafkaTemplate<Object, ClientProduct> kafkaTemplate) {
        this.clientProductRepository = clientProductRepository;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
