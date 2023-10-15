package com.example.storeservice.DTOs;

import lombok.Data;

@Data
public class StockDTO {

    private Long stockId;
    private Long productId;
    private Long storeId;
    private int quantity;
    private String dateAdded;
}

