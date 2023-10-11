package com.example.storeservice.DTOs;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OrderItemsResponse {

    private Long id;
    private Long storeId;
    private String name;
    private double price;
    private boolean available;
    private int quantity;
}
