package com.example.storeservice.services;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.DTOs.ProductDTO;
import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.DTOs.StoreDTO;
import com.example.storeservice.entities.Product;
import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.entities.Store;
import com.example.storeservice.mappers.ProductConsumptionMapper;
import com.example.storeservice.mappers.ProductMapper;
import com.example.storeservice.mappers.StockMapper;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.ProductConsumptionRepository;
import com.example.storeservice.repositories.ProductRepository;
import com.example.storeservice.repositories.StockRepository;
import com.example.storeservice.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private ProductConsumptionMapper productConsumptionMapper;

    @Override
    public StoreDTO createStore(StoreDTO storeDTO) {
        Store store = storeMapper.ToStore(storeDTO);
        store = storeRepository.save(store);
        return storeMapper.ToStoreDTO(store);
    }

    @Override
    public List<StoreDTO> getAllStores() {
        return storeRepository
                .findAll()
                .stream()
                .map(storeMapper::ToStoreDTO)
                .toList();
    }

    @Override
    public StoreDTO getStoreById(Long storeId) {
        return storeRepository
                .findById(storeId)
                .map(storeMapper::ToStoreDTO)
                .orElse(null);
    }

    @Override
    public List<ProductDTO> searchProductsByName(String productName) {
        List<Product> matchingProducts = productRepository.findAllByproductNameStartsWith(productName);
        return matchingProducts
                .stream()
                .map(productMapper::ToProductDTO)
                .toList();
    }

    @Override
    @Async
    public StockDTO addStock(Long storeId, Long productId, int quantity) {

        Store store = storeRepository.findById(storeId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        if( product.getStore() != store)
        {
            return null;
        }

        Stock stock = stockRepository.findByStoreAndProduct(store, productRepository.findById(productId).orElse(null)).orElseThrow();

        stock.setQuantity(stock.getQuantity() + quantity);

        LocalDate date = LocalDate.now();
        stock.setDateAdded(date.toString());

        stock = stockRepository.save(stock);

        return stockMapper.ToStockDTO(stock);
    }

    @Override
    public List<ProductConsumptionDTO> consumeProducts(Long storeId, Long productId, int quantity) {

        Store store = storeRepository.findById(storeId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);


        if( product.getStore() != store)
        {
            return null;
        }

        Stock stock = stockRepository.findByStoreAndProduct(store, productRepository.findById(productId).orElse(null)).orElseThrow();

        stock.setQuantity(stock.getQuantity() - quantity);

        LocalDate date = LocalDate.now();

        stock = stockRepository.save(stock);

        ProductConsumption productConsumption = new ProductConsumption();
        productConsumption.setProduct(product);
        productConsumption.setStore(store);
        productConsumption.setQuantityConsumed(quantity);
        productConsumption.setDateConsumed(date.toString());

        productConsumptionRepository.save(productConsumption);

        return productConsumptionRepository
                .findAll()
                .stream()
                .map(productConsumptionMapper::ToProductConsumptionDTO)
                .toList();
    }

    @Override
    public List<ProductConsumptionDTO> getAllProductConsumptions() {

        return productConsumptionRepository
                .findAll()
                .stream()
                .map(productConsumptionMapper::ToProductConsumptionDTO)
                .toList();
    }

    @Override
    public List<ProductConsumptionDTO> getProductConsumptionsByStoreId(Long storeId) {

        Store store = storeRepository.findById(storeId).orElse(null);

        System.out.println(productConsumptionRepository.findAllByStore(store).size() + productConsumptionRepository.findAllByStore(store).toString());
        return productConsumptionRepository
                .findAllByStore(store)
                .stream()
                .map(productConsumptionMapper::ToProductConsumptionDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getAllProductsByStoreId(Long storeId) {

        Store store = storeRepository.findById(storeId).orElse(null);

        return productRepository
                .findAllByStoreId(storeId)
                .stream()
                .map(productMapper::ToProductDTO)
                .toList();
    }
}
