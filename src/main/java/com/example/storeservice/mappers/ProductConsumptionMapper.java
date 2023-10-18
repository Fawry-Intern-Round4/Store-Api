package com.example.storeservice.mappers;

import com.example.storeservice.dtos.ProductConsumptionDTO;
import com.example.storeservice.entities.ProductConsumption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductConsumptionMapper {

    ProductConsumptionDTO toProductConsumptionDTO(ProductConsumption productConsumption);
}
