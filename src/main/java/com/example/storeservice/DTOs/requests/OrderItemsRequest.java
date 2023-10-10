package com.example.storeservice.DTOs.requests;

import lombok.Data;

@Data
public class OrderItemsRequest {

    private Long productId;
    private Long storeId;
    private int quantity;
}
