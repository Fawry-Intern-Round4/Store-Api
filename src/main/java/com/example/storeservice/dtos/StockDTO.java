package com.example.storeservice.dtos;

import lombok.Data;

@Data
public class StockDTO {

    private Long id;
    private Long productId;
    private Long storeId;
    private int quantity;
    private String dateAdded;
}

