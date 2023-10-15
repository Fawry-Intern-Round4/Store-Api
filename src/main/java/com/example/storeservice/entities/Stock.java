package com.example.storeservice.entities;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

    @CreationTimestamp
    private Instant dateAdded;
}
