package com.example.storeservice.services;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.StockDTO;

import java.util.List;

public interface StockService {
    StockDTO addStock(ItemRequest itemRequest);
    List<StockDTO> getStockByStoreId(Long storeId);
    void consumeStock(List<ItemRequest> itemRequest);
}
