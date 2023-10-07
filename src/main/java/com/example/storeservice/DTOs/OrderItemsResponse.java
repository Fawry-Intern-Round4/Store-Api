package com.example.storeservice.DTOs;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OrderItemsResponse {

    private Long productId;
    private Long storeId;
    private String productName;
    private double price;
    private boolean available;
    private int quantity;
}
