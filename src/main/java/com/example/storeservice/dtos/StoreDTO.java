package com.example.storeservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class StoreDTO {

    private Long id;

    @NotBlank(message = "Store name is mandatory")
    private String name;

    @NotBlank(message = "Store location is mandatory")
    private String location;
}

