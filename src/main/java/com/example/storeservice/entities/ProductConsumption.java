package com.example.storeservice.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "product_consumptions")
@Data
public class ProductConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consumptionId;

    private Long productId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private int quantityConsumed;

    private String dateConsumed;
}



