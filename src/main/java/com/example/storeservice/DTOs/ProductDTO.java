package com.example.storeservice.DTOs;

import com.example.storeservice.entities.Store;
import lombok.Data;

@Data
public class ProductDTO {

    private Long productId;
    private String productName;
    private String category;
    private Store store;
}

