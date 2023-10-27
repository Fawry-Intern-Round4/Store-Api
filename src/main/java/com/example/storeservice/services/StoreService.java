package com.example.storeservice.services;

import com.example.storeservice.dtos.*;

import java.util.List;
import java.util.Set;

public interface StoreService {

    StoreDTO createStore(StoreDTO storeDTO);

    List<StoreDTO> getAllStores();

    StoreDTO getStoreById(Long storeId);

    List<ProductResponse> getAllProductsByStoreId(Long storeId);

    void checkIfStoreExists(Long storeId);

    void checkIfStoresExists(Set<Long> ids);

}
