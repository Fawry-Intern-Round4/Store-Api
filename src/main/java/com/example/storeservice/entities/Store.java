package com.example.storeservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "stores")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store name is mandatory")
    private String name;

    @NotBlank(message = "Store location is mandatory")
    private String location;
}

