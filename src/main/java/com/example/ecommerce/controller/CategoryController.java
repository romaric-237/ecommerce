package com.example.ecommerce.controller;

import com.example.ecommerce.entities.CategoryEntity;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/categories")
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable int id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categories/{id}/products")
    public List<ProductEntity> getProductsByCategory(@PathVariable int id) {
        return productRepository.findByCategoryId(id);
    }
}