package com.example.storeservice.services;

import com.example.storeservice.dtos.*;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.entities.Store;
import com.example.storeservice.exception.IdsException;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.StockRepository;
import com.example.storeservice.repositories.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final WebClientService webClientService;
    private final StoreRepository storeRepository;
    private final StockRepository stockRepository;
    private final StoreMapper storeMapper;


    @Override
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = storeMapper.toStore(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.toStoreDTO(store);
    }

    @Override
    public List<StoreDTO> getAllStores() {
        return storeRepository
                .findAll()
                .stream()
                .map(storeMapper::toStoreDTO)
                .toList();
    }

    @Override
    public StoreDTO getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("store id does not exist"));
        return storeMapper.toStoreDTO(store);
    }

    @Override
    public List<ProductResponse> getAllProductsByStoreId(Long id) {

        List<Stock> stockList = stockRepository.findByStore_Id(id);
        Set<Long> productIds = new HashSet<>(stockList
                .stream()
                .map(Stock::getProductId)
                .toList());

        return webClientService.getProducts(productIds);
    }


    @Override
    public void checkIfStoreExists(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new IllegalArgumentException("store id does not exist");
        }
    }

    @Override
    public void checkIfStoresExists(Set<Long> ids){
        List<Store> stores = storeRepository.findByIdIn(ids);
        if (stores.size() != ids.size()) {
            Set<Long> requestStoreIds = new HashSet<>(stores.stream()
                    .map(Store::getId)
                    .toList());
            ids.removeAll(requestStoreIds);
            throw new IdsException("some stores ids do not exist", ids);
        }
    }
}
