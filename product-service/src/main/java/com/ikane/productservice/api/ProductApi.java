package com.ikane.productservice.api;

import com.ikane.productservice.domain.Product;
import com.ikane.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ProductApi {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path="/products")
    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    @PostMapping(path="/products")
    public Product save(@RequestBody Product product) {
        return this.productRepository.save(product);
    }
}
