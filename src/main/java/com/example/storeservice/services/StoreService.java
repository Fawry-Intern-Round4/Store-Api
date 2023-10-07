package com.example.storeservice.services;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.DTOs.ProductDTO;
import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.DTOs.StoreDTO;

import java.util.List;

public interface StoreService {

    StoreDTO createStore(StoreDTO storeDTO);

    List<StoreDTO> getAllStores();

    StoreDTO getStoreById(Long storeId);

    List<ProductDTO> searchProductsByName(String productName);

    StockDTO addStock(Long storeId, Long productId, int quantity);

    List<ProductConsumptionDTO> consumeProducts(Long storeId, Long productId, int quantity);

    List<ProductConsumptionDTO> getAllProductConsumptions();

    List<ProductConsumptionDTO> getProductConsumptionsByStoreId(Long storeId);

    List<ProductDTO> getAllProductsByStoreId(Long storeId);
}
