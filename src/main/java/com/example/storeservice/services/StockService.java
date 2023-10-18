package com.example.storeservice.services;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.StockDTO;

import java.util.List;

public interface StockService {
    StockDTO addStock(ItemRequest itemRequest);

    void validateStock(List<ItemRequest> itemRequest);

    void consumeStock(List<ItemRequest> itemRequest);
}
