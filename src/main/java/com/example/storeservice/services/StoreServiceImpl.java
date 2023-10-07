package com.example.storeservice.services;

import com.example.storeservice.DTOs.*;
import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.entities.Store;
import com.example.storeservice.mappers.ProductConsumptionMapper;
import com.example.storeservice.mappers.StockMapper;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.ProductConsumptionRepository;
import com.example.storeservice.repositories.StockRepository;
import com.example.storeservice.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private ProductConsumptionMapper productConsumptionMapper;

    private final WebClient webClient;

    public StoreServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/products").build();
    }

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

//    @Override
//    public List<ProductDTO> searchProductsByName(String productName) {
//        List<Product> matchingProducts = productRepository.findAllByproductNameStartsWith(productName);
//        return matchingProducts
//                .stream()
//                .map(productMapper::ToProductDTO)
//                .toList();
//    }

    @Override
    public List<StockDTO> addStock(List<OrderItemsRequest> orderItemsRequest) {

        List<Long> productIds = orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getProductId)
                .toList();

        List<OrderItemsResponse> orderItemsResponse = webClient
                .post()
                .uri("/getProductsByIds")
                .bodyValue(productIds)
                .retrieve()
                .bodyToFlux(OrderItemsResponse.class)
                .collectList()
                .block();

        LocalDate date = LocalDate.now();
        List<StockDTO> stockDTOList = new ArrayList<>();

        for(OrderItemsResponse orderItemsResponseCheck : orderItemsResponse)
        {
            Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsResponseCheck.getStoreId(),orderItemsResponseCheck.getProductId());
            if ( stock != null)
            {
                stock.setQuantity(stock.getQuantity() + orderItemsResponseCheck.getQuantity());
                stock.setDateAdded(date.toString());
                stockRepository.save(stock);
            }
            else
            {
                stock = new Stock();
                stock.setProductId(orderItemsResponseCheck.getProductId());
                stock.setStore(storeRepository.getReferenceById(orderItemsResponseCheck.getStoreId()));
                stock.setQuantity(orderItemsResponseCheck.getQuantity());
                stock.setDateAdded(date.toString());
                stockRepository.save(stock);
            }
            stockDTOList.add(stockMapper.ToStockDTO(stock));
        }

        return stockDTOList;
    }

    @Override
    public List<OrderItemsResponse> consumeProducts(List<OrderItemsRequest> orderItemsRequest) {

        List<Long> productIds = orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getProductId)
                .toList();


        List<OrderItemsResponse> orderItemsResponse = webClient
                                .post()
                                .uri("/getProductsByIds")
                                .bodyValue(productIds)
                                .retrieve()
                                .bodyToFlux(OrderItemsResponse.class)
                                .collectList()
                                .block();

        LocalDate date = LocalDate.now();

        for(OrderItemsResponse orderItemsResponseCheck : orderItemsResponse)
        {
            Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsResponseCheck.getStoreId(),orderItemsResponseCheck.getProductId());
            if ( stock.getQuantity() >= orderItemsResponseCheck.getQuantity())
            {
                orderItemsResponseCheck.setAvailable(true);
                stock.setQuantity(stock.getQuantity() - orderItemsResponseCheck.getQuantity());
                stockRepository.save(stock);
                ProductConsumption productConsumption = new ProductConsumption();
                productConsumption.setProductId(orderItemsResponseCheck.getProductId());
                productConsumption.setStore(storeRepository.getReferenceById(orderItemsResponseCheck.getStoreId()));
                productConsumption.setQuantityConsumed(orderItemsResponseCheck.getQuantity());
                productConsumption.setDateConsumed(date.toString());
                productConsumptionRepository.save(productConsumption);
            }
            else
            {
                orderItemsResponseCheck.setAvailable(false);
            }
        }

        return orderItemsResponse;
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

//    @Override
//    public List<ProductDTO> getAllProductsByStoreId(Long storeId) {
//
//        Store store = storeRepository.findById(storeId).orElse(null);
//
//        return productRepository
//                .findAllByStoreId(storeId)
//                .stream()
//                .map(productMapper::ToProductDTO)
//                .toList();
//    }
}
