package com.example.storeservice.DTOs;

import com.example.storeservice.entities.Product;
import com.example.storeservice.entities.Store;
import lombok.Data;

@Data
public class StockDTO {

    private Long stockId;
    private Product product;
    private Store store;
    private int quantity;
    private String dateAdded;
}

