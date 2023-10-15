package com.example.storeservice.services;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.DTOs.StoreDTO;
import com.example.storeservice.DTOs.productResponse;
import com.example.storeservice.DTOs.requests.OrderItemsRequest;
import com.example.storeservice.entities.ProductConsumption;
import com.example.storeservice.entities.Stock;
import com.example.storeservice.entities.Store;
import com.example.storeservice.error.IdsError;
import com.example.storeservice.exception.IdsException;
import com.example.storeservice.mappers.ProductConsumptionMapper;
import com.example.storeservice.mappers.StockMapper;
import com.example.storeservice.mappers.StoreMapper;
import com.example.storeservice.repositories.ProductConsumptionRepository;
import com.example.storeservice.repositories.StockRepository;
import com.example.storeservice.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public StockDTO addStock(OrderItemsRequest orderItemsRequest) {
        Long productId = orderItemsRequest.getProductId();
        Long storeId = orderItemsRequest.getStoreId();
        int quantity = orderItemsRequest.getQuantity();

        webClient.build().get()
                .uri("lb://product-api/product", uriBuilder -> uriBuilder
                        .pathSegment(productId.toString()).build()
                ).retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    throw new IllegalArgumentException("product id does not exist");
                })
                .toEntity(String.class)
                .block();
        if (!storeRepository.existsById(orderItemsRequest.getStoreId())) {
            throw new IllegalArgumentException("store id does not exist");
        }
        Stock stock = stockRepository.findByStore_StoreIdAndProductId(storeId, productId);
        if (stock != null) {
            stock.setQuantity(stock.getQuantity() + orderItemsRequest.getQuantity());
            stockRepository.save(stock);
        } else {
            stock = new Stock();
            stock.setProductId(productId);
            stock.setStore(storeRepository.getReferenceById(storeId));
            stock.setQuantity(quantity);
            stock = stockRepository.save(stock);
        }
        return stockMapper.ToStockDTO(stock);
    }

    @Override
    public void consumeProducts(List<OrderItemsRequest> orderItemsRequest) {
        Set<Long> productIds = new HashSet<>(orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getProductId)
                .toList());
        Set<Long> storeIds = new HashSet<>(orderItemsRequest
                .stream()
                .map(OrderItemsRequest::getStoreId)
                .toList());
        webClient.build().get()
                .uri("lb://product-api/product", uriBuilder -> uriBuilder
                        .queryParam("ids", productIds).build()
                ).retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                     clientResponse
                             .bodyToMono(IdsError.class)
                             .flatMap(idsError ->
                                     Mono.error(new IdsException(idsError.getMessage(), idsError.getIds()))
                             )
                )
                .bodyToMono(String.class)
                .block();
        List<Store> stores = storeRepository.findByStoreIdIn(storeIds);
        if (stores.size() != storeIds.size()) {
            Set<Long> requestStoreIds = new HashSet<>(stores.stream()
                    .map(Store::getStoreId)
                    .toList());
            storeIds.removeAll(requestStoreIds);
            throw new IdsException("some stores ids do not exist", storeIds);
        }

        List<Long> ids = new ArrayList<>();
        for (OrderItemsRequest orderItem : orderItemsRequest) {
            Stock stock = stockRepository.findByStore_StoreIdAndProductId(orderItem.getStoreId(), orderItem.getProductId());
            if (stock == null || orderItem.getQuantity() > stock.getQuantity()) {
                ids.add(orderItem.getProductId());
            }
        }

        if (!ids.isEmpty()) {
            throw new IdsException("some products out of stock", new HashSet<>(ids));
        }

        LocalDate date = LocalDate.now();
        for (OrderItemsRequest orderItem : orderItemsRequest) {
            Stock stock = stockRepository.findByStore_StoreIdAndProductId(orderItem.getStoreId(), orderItem.getProductId());
            stock.setQuantity(stock.getQuantity() - orderItem.getQuantity());
            ProductConsumption productConsumption = new ProductConsumption();
            productConsumption.setProductId(orderItem.getProductId());
            productConsumption.setStore(storeRepository.getReferenceById(orderItem.getStoreId()));
            productConsumption.setQuantityConsumed(orderItem.getQuantity());
            productConsumption.setDateConsumed(date.toString());
            productConsumptionRepository.save(productConsumption);
        }
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

        List<Stock> stockList = stockRepository.findByStore_StoreId(storeId);

        List<Long> productIds = stockList
                .stream()
                .map(Stock::getProductId)
                .toList();

        List<productResponse> productResponseList = webClient.build()
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