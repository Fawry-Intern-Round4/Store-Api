package com.example.storeservice.mappers;

import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.entities.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockDTO ToStockDTO(Stock stock);
}
