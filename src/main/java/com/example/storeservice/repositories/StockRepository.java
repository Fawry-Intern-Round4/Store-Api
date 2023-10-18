package com.example.storeservice.repositories;

import com.example.storeservice.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByStore_Id(Long id);

    Stock findByStore_IdAndProductId(Long id, Long productId);

}
