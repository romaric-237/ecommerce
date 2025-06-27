package com.example.ecommerce.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Integer productId;
    private String productName;
    private String productImage;
    private String productBrand;
    private int quantity;
    private Double price;
    private Double totalPrice; // quantity * price
} 