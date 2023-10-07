package com.example.storeservice.DTOs;

import com.example.storeservice.entities.Store;
import lombok.Data;

@Data
public class ProductConsumptionDTO {

    private Long consumptionId;
    private Long productId;
    private Store store;
    private int quantityConsumed;
    private String dateConsumed;
}
