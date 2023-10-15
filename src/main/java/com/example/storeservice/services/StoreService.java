package com.example.storeservice.services;

import com.example.storeservice.DTOs.*;
import com.example.storeservice.DTOs.requests.OrderItemsRequest;

import java.util.List;

public interface StoreService {

    StoreDTO createStore(StoreDTO storeDTO);

    List<StoreDTO> getAllStores();

    StoreDTO getStoreById(Long storeId);

    StockDTO addStock(OrderItemsRequest orderItemsRequest);

    void consumeProducts(List<OrderItemsRequest> orderItemsRequest);

    List<ProductConsumptionDTO> getAllProductConsumptions();

    List<ProductConsumptionDTO> getProductConsumptionsByStoreId(Long storeId);

    List<productResponse> getAllProductsByStoreId(Long storeId);
}
