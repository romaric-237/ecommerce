package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.ProductDTO;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        // Mapper chaque ProductEntity en ProductDTO
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return productDTOs;
    }


    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);

        return new ProductDTO(productEntity.get()); // Mapper le ProductEntity trouvé en ProductDTO

    }

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsByCategory(@PathVariable int id) {
        List<ProductEntity> products = productRepository.findByCategoryId(id);
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
        return productDTOs;
    }
}
