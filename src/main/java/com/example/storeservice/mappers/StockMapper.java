package com.example.storeservice.mappers;

import com.example.storeservice.dtos.StockDTO;
import com.example.storeservice.entities.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "store.id", target = "storeId")
    StockDTO toStockDTO(Stock stock);
}
