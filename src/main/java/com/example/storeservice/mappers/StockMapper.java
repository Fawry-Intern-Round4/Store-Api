package com.example.storeservice.mappers;

import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.entities.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "store.storeId", target = "storeId")
    StockDTO ToStockDTO(Stock stock);
}
