package com.example.storeservice.entities;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "stocks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID is mandatory")
    private Long productId;

    @NotNull(message = "Quantity is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Quantity must be a number")
    @PositiveOrZero(message = "Quantity must be positive")
    private int quantity;

    @CreationTimestamp
    private Instant dateAdded;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
