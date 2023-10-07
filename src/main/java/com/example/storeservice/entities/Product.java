package com.example.storeservice.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String category;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
