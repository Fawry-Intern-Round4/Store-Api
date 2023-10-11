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

//    @Override
//    public List<ProductDTO> searchProductsByName(String productName) {
//        List<Product> matchingProducts = productRepository.findAllByproductNameStartsWith(productName);
//        return matchingProducts
//                .stream()
//                .map(productMapper::ToProductDTO)
//                .toList();
//    }

    //TODO: make it only take one product id and quantity and store id
    @Override
    public List<StockDTO> addStock(List<OrderItemsRequest> orderItemsRequest) {
        //TODO: check if product id exists in product api
        //TODO: check if store id exists in store api
        //TODO: if it already exists in stock api then add the quantity to the existing quantity
        //TODO: if it doesn't exist in stock api then create a new stock with the quantity



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

        LocalDate date = LocalDate.now();
        List<StockDTO> stockDTOList = new ArrayList<>();
        for (OrderItemsResponse orderItemsResponseCheck : orderItemsResponse){
            for (OrderItemsRequest orderItemsRequestCheck : orderItemsRequest){
                if (orderItemsResponseCheck.getId().equals(orderItemsRequestCheck.getProductId())){
                    orderItemsResponseCheck.setStoreId(orderItemsRequestCheck.getStoreId());
                    orderItemsResponseCheck.setQuantity(orderItemsRequestCheck.getQuantity());
                }
            }
        }
        for (OrderItemsResponse orderItemsResponseCheck : orderItemsResponse) {
            Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsResponseCheck.getStoreId() , orderItemsResponseCheck.getId());
            if (stock != null) {
                stock.setQuantity(stock.getQuantity() + orderItemsResponseCheck.getQuantity());
                stock.setDateAdded(date.toString());
                stockRepository.save(stock);
            } else {
                stock = new Stock();
                stock.setProductId(orderItemsResponseCheck.getId());
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
        //TODO: check if product ids exist in product api
        //TODO: check if store ids exist in store api
        //TODO: check if the quantity is available in stock api
        //TODO: if it is available then consume the quantity from the stock
        //TODO: if it is not available then return list of products that are not available

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


        LocalDate date = LocalDate.now();

        for (OrderItemsResponse orderItemsResponseCheck : orderItemsResponse) {
            Stock stock = stockRepository.findByProductIdAndStoreId(orderItemsResponseCheck.getStoreId(), orderItemsResponseCheck.getId());
            if (stock.getQuantity() >= orderItemsResponseCheck.getQuantity()) {
                orderItemsResponseCheck.setAvailable(true);
                stock.setQuantity(stock.getQuantity() - orderItemsResponseCheck.getQuantity());
                stockRepository.save(stock);
                ProductConsumption productConsumption = new ProductConsumption();
                productConsumption.setProductId(orderItemsResponseCheck.getId());
                productConsumption.setStore(storeRepository.getReferenceById(orderItemsResponseCheck.getStoreId()));
                productConsumption.setQuantityConsumed(orderItemsResponseCheck.getQuantity());
                productConsumption.setDateConsumed(date.toString());
                productConsumptionRepository.save(productConsumption);
            } else {
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
