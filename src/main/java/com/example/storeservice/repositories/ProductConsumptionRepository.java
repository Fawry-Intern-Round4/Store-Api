package com.example.storeservice.repositories;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.entities.Product;
import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductConsumptionRepository extends JpaRepository<ProductConsumption, Long> {

    List<ProductConsumption> findAllByStore(Store store);
}
