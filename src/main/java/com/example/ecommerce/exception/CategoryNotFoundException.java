package com.example.ecommerce.exception;

public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(String message) {
        super(message);
    }
    
    public CategoryNotFoundException(int categoryId) {
        super("Catégorie non trouvée avec l'ID: " + categoryId);
    }
    
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 