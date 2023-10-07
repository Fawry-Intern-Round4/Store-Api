package com.example.storeservice.repositories;

import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductConsumptionRepository extends JpaRepository<ProductConsumption, Long> {

    List<ProductConsumption> findAllByStore(Store store);
}
