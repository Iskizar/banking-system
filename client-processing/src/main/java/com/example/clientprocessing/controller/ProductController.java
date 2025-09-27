package com.example.clientprocessing.controller;

import com.example.clientprocessing.DTO.CreateCardRequest;
import com.example.clientprocessing.DTO.ProductDTO;
import com.example.clientprocessing.model.ClientProduct;
import com.example.clientprocessing.model.Product;
import com.example.clientprocessing.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setKey(dto.key());
        return productService.createProduct(product);
    }
    @PostMapping("/create-card/{clientId}")
    public ClientProduct createCard(
            @PathVariable("clientId") Long clientId,
            @RequestBody CreateCardRequest request
    ) {
        return productService.createCard(clientId, request.getCardId());
    }
    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
