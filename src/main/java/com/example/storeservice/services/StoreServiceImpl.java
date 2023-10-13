package com.example.storeservice.services;

import com.example.storeservice.DTOs.*;
import com.example.storeservice.DTOs.requests.OrderItemsRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final WebClient.Builder webClient;
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

    public StoreServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient;
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

    @Override
    public String addStock(OrderItemsRequest orderItemsRequest) {

        List<Long> productIds = List.of(orderItemsRequest.getProductId());

        List<OrderItemsResponse> orderItemsResponse = webClient.build()
                .get()
                .uri("lb://product-api/product", uriBuilder -> uriBuilder
                        .queryParam("ids", productIds)
                        .build())
                .retrieve()
                .bodyToFlux(OrderItemsResponse.class)
                .collectList()
                .block();

        Store store = storeRepository.findById(orderItemsRequest.getStoreId()).orElse(null);

        if (store == null){
            return "NOT FOUND";
        }

        Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsRequest.getStoreId() , orderItemsRequest.getProductId());

        if (stock != null){
            stock.setQuantity(stock.getQuantity() + orderItemsRequest.getQuantity());
            stock.setDateAdded(LocalDate.now().toString());
            stockRepository.save(stock);
            return "UPDATED";
        }

        stock = new Stock();
        stock.setProductId(orderItemsRequest.getProductId());
        stock.setStore(store);
        stock.setQuantity(orderItemsRequest.getQuantity());
        stock.setDateAdded(LocalDate.now().toString());
        stockRepository.save(stock);
        return "CREATED";
    }

    @Override
    public List<OrderItemsResponse> consumeProducts(List<OrderItemsRequest> orderItemsRequest) {

        List<Long> productIds = orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getProductId)
                .toList();

        List<OrderItemsResponse> orderItemsResponse = webClient.build()
                .get()
                .uri("lb://product-api/product", uriBuilder -> uriBuilder
                        .queryParam("ids", productIds)
                        .build())
                .retrieve()
                .bodyToFlux(OrderItemsResponse.class)
                .collectList()
                .block();

        List<Long> storeIds = orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getStoreId)
                .toList();

        for (Long productId : productIds) {
            for (int i = 0; i < storeIds.size(); i++) {
                if ( stockRepository.findByProductIdAndStoreId(storeIds.get(i), productId) != null){
                    break;
                }
                else if (i == storeIds.size() - 1){
                    // product not found in any store
                }
            }
        }

        List<OrderItemsResponse> productsNotAvailable = new ArrayList<>();

        for (OrderItemsResponse orderItemsResponseCheck : orderItemsResponse) {
            Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsResponseCheck.getStoreId(), orderItemsResponseCheck.getId());
            if (stock.getQuantity() >= orderItemsResponseCheck.getQuantity()) {
                orderItemsResponseCheck.setAvailable(true);
                stock.setQuantity(stock.getQuantity() - orderItemsResponseCheck.getQuantity());
            } else {
                orderItemsResponseCheck.setAvailable(false);
                productsNotAvailable.add(orderItemsResponseCheck);
            }
        }

        return productsNotAvailable;
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
    public List<productResponse> getAllProductsByStoreId(Long storeId) {

        List<Stock> stockList = stockRepository.findByStoreId(storeId);

        List<Long> productIds = stockList
                .stream()
                .map(Stock::getProductId)
                .toList();

        List<productResponse> productResponseList =  webClient.build()
                .get()
                .uri("lb://product-api/product", uriBuilder -> uriBuilder
                        .queryParam("ids", productIds)
                        .build())
                .retrieve()
                .bodyToFlux(productResponse.class)
                .collectList()
                .block();

        return productResponseList;
    }
}
