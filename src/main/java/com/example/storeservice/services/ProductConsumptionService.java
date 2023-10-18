package com.example.storeservice.services;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.ProductConsumptionDTO;

import java.util.List;

public interface ProductConsumptionService {
    void createProductConsumption(ItemRequest itemRequest);
    List<ProductConsumptionDTO> getAllProductConsumptions();
    List<ProductConsumptionDTO> getProductConsumptionsByStoreId(Long storeId);
}
