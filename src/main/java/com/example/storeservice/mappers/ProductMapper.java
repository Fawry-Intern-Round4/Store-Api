package com.example.storeservice.mappers;

import com.example.storeservice.DTOs.ProductDTO;
import com.example.storeservice.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO ToProductDTO(Product product);
}
