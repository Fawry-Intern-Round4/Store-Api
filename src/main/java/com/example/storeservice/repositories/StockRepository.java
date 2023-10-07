package com.example.storeservice.repositories;

import com.example.storeservice.entities.Product;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByStoreAndProduct(Store store, Product product);
}
