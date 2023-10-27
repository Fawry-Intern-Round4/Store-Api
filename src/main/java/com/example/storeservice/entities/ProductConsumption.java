package com.example.storeservice.entities;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "product_consumptions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID is mandatory")
    private Long productId;

    @NotNull(message = "Quantity consumed is mandatory")
    private int quantityConsumed;

    @CreationTimestamp
    private Instant dateConsumed;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}



