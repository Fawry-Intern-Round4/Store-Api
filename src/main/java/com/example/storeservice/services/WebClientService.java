package com.example.storeservice.services;

import com.example.storeservice.dtos.ProductResponse;

import java.util.List;
import java.util.Set;

public interface WebClientService {
    List<ProductResponse> getProducts(Set<Long> productIds);

    void checkIfProductsExist(Long productId);
}
