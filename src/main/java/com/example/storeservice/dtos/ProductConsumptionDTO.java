package com.example.storeservice.dtos;

import com.example.storeservice.entities.Store;
import lombok.Data;

import java.time.Instant;

@Data
public class ProductConsumptionDTO {

    private Long id;
    private Long productId;
    private Store store;
    private int quantityConsumed;
    private Instant dateConsumed;
}
