package com.example.storeservice.repositories;

import com.example.storeservice.entities.Product;
import com.example.storeservice.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByproductNameStartsWith(String productName);

    @Query("SELECT p FROM Product p WHERE p.store.storeId = ?1")
    List<Product> findAllByStoreId(Long storeId);
}
