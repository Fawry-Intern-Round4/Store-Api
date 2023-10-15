package com.example.storeservice.repositories;

import com.example.storeservice.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByStore_StoreIdAndProductId(Long storeId, Long productId);

    List<Stock> findByStore_StoreId(Long storeId);

}
