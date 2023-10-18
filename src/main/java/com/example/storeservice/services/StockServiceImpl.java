package com.example.storeservice.services;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.StockDTO;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.exception.IdsException;
import com.example.storeservice.mappers.StockMapper;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final WebClientService webClientService;
    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final StoreMapper storeMapper;
    private final StoreService storeService;
    private final ProductConsumptionService productConsumptionService;

    @Override
    public StockDTO addStock(ItemRequest itemRequest) {
        webClientService.checkIfProductsExist(itemRequest.getProductId());
        storeService.checkIfStoreExists(itemRequest.getStoreId());
        Stock stock = stockRepository.findByStore_IdAndProductId(itemRequest.getStoreId(), itemRequest.getProductId());
        if (stock != null) {
            stock.setQuantity(stock.getQuantity() + itemRequest.getQuantity());
            return stockMapper.toStockDTO(stockRepository.save(stock));
        }
        return stockMapper.toStockDTO(addNewStock(itemRequest));
    }

    @Override
    public List<StockDTO> getStockByStoreId(Long storeId) {
        return stockRepository.findByStore_Id(storeId)
                .stream()
                .map(stockMapper::toStockDTO)
                .toList();
    }

    @Override
    public void consumeStock(List<ItemRequest> itemRequest) {
           webClientService.getProducts(getProductIds(itemRequest));
           storeService.checkIfStoresExists(getStoreIds(itemRequest));
           checkIfProductsOutOfStock(itemRequest);
           for (ItemRequest item : itemRequest) {
               consumeProductFromStock(item);
               productConsumptionService.createProductConsumption(item);
           }
    }

    private Stock addNewStock(ItemRequest itemRequest) {
        return stockRepository.save(Stock.builder()
                .productId(itemRequest.getProductId())
                .quantity(itemRequest.getQuantity())
                .store(storeMapper.toStore(storeService.getStoreById(itemRequest.getStoreId())))
                .build());
    }
    private Set<Long> getProductIds(List<ItemRequest> itemRequests) {
        Set<Long> productIds = new HashSet<>();
        itemRequests.forEach(itemRequest -> productIds.add(itemRequest.getProductId()));
        return productIds;
    }
    private Set<Long> getStoreIds(List<ItemRequest> itemRequests) {
        Set<Long> storeIds = new HashSet<>();
        itemRequests.forEach(itemRequest -> storeIds.add(itemRequest.getStoreId()));
        return storeIds;
    }

    private void checkIfProductsOutOfStock(List<ItemRequest> itemsRequest) {
        List<Long> ids = new ArrayList<>();
        for (ItemRequest itemRequest : itemsRequest) {
            Stock stock = stockRepository.findByStore_IdAndProductId(itemRequest.getStoreId(), itemRequest.getProductId());
            if (stock == null || itemRequest.getQuantity() > stock.getQuantity()) {
                ids.add(itemRequest.getProductId());
            }
        }
        if (!ids.isEmpty()) {
            throw new IdsException("some products out of stock", new HashSet<>(ids));
        }
    }

    private void consumeProductFromStock(ItemRequest itemRequest){
        Stock stock = stockRepository.findByStore_IdAndProductId(itemRequest.getStoreId(), itemRequest.getProductId());
        stock.setQuantity(stock.getQuantity() - itemRequest.getQuantity());
        stockRepository.save(stock);
    }

}
