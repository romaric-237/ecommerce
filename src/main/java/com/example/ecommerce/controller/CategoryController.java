package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.CategoryDTO;
import com.example.ecommerce.controller.dto.ProductDTO;
import com.example.ecommerce.entities.CategoryEntity;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
        return categoryDTOs;
    }

    @GetMapping("/category/{id}")
    public CategoryDTO getCategoryById(@PathVariable int id) {

        return categoryRepository.findById(id).map(CategoryDTO::new).orElse(null);
    }

//    @GetMapping("/products/category/{id}")
//    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable int id) {
//        List<ProductEntity> products = productRepository.findByCategoryId(id);
//        List<ProductDTO> productDTOs = products.stream()
//                .map(ProductDTO::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(productDTOs);
//    }

}