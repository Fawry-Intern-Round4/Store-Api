package com.example.storeservice.dtos;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double price;
    private String image;
}
