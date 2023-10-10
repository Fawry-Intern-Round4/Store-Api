package com.example.storeservice.DTOs;

import lombok.Data;

@Data
public class productResponse {

    private Long productId;
    private String productName;
    private double price;
    private String description;
    private String photoUrl;
}
