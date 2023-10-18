package com.example.storeservice.services;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.ProductConsumptionDTO;
import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.mappers.ProductConsumptionMapper;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.ProductConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductConsumptionServiceImpl implements ProductConsumptionService {
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    private final ProductConsumptionRepository productConsumptionRepository;
    private final ProductConsumptionMapper productConsumptionMapper;

    @Override
    public void createProductConsumption(ItemRequest itemRequest) {
        ProductConsumption productConsumption = ProductConsumption.builder()
                .productId(itemRequest.getProductId())
                .quantityConsumed(itemRequest.getQuantity())
                .store(storeMapper.toStore(storeService.getStoreById(itemRequest.getStoreId())))
                .build();
        productConsumptionRepository.save(productConsumption);
    }

    @Override
    public List<ProductConsumptionDTO> getAllProductConsumptions() {
        return productConsumptionRepository
                .findAll()
                .stream()
                .map(productConsumptionMapper::toProductConsumptionDTO)
                .toList();
    }

    @Override
    public List<ProductConsumptionDTO> getProductConsumptionsByStoreId(Long storeId) {
        return productConsumptionRepository
                .findByStore_Id(storeId)
                .stream()
                .map(productConsumptionMapper::toProductConsumptionDTO)
                .toList();
    }
}
