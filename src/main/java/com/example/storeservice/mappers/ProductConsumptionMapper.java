package com.example.storeservice.mappers;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.entities.ProductConsumption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductConsumptionMapper {

    ProductConsumptionDTO ToProductConsumptionDTO(ProductConsumption productConsumption);
}
