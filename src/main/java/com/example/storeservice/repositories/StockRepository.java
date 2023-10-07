package com.example.storeservice.repositories;

import com.example.storeservice.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.productId = ?1 AND s.store.storeId = ?2")
    Stock findByProductIdAndStoreId(Long productId, Long storeId);
}
