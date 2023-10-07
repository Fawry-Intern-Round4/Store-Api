package com.example.storeservice.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private Long productId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private int quantity;

    private String dateAdded;
}
