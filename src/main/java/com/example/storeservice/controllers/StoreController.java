package com.example.storeservice.controllers;

import com.example.storeservice.DTOs.*;
import com.example.storeservice.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public List<StoreDTO> getAllStores() {
        return storeService.getAllStores();
    }

//    @GetMapping("/products")
//    public List<ProductDTO> getAllProductsByStoreId(@RequestParam Long storeId) {
//        return storeService.getAllProductsByStoreId(storeId);
//    }

    @PostMapping
    public StoreDTO createStore(@RequestBody StoreDTO storeDTO) {
        return storeService.createStore(storeDTO);
    }

    @GetMapping("/get")
    public StoreDTO getStoreById(@RequestParam Long storeId) {
        return storeService.getStoreById(storeId);
    }

//    @GetMapping("/search")
//    public List<ProductDTO> searchStoresByName(@RequestParam String productName) {
//        return storeService.searchProductsByName(productName);
//    }

    @PutMapping("/addStock")
    public List<StockDTO> addStock(@RequestBody List<OrderItemsRequest> orderItemsRequest) {
        return storeService.addStock(orderItemsRequest);
    }

    @PostMapping("/consumeProducts")
    public List<OrderItemsResponse> consumeProducts(@RequestBody List<OrderItemsRequest> orderItemsRequests) {
        return storeService.consumeProducts(orderItemsRequests);
    }

    @GetMapping("/getAllProductConsumptions")
    public List<ProductConsumptionDTO> getAllProductConsumptions() {
        return storeService.getAllProductConsumptions();
    }

    @GetMapping("/getProductConsumptionsByStoreId")
    public List<ProductConsumptionDTO> getProductConsumptionsByStoreId(@RequestParam Long storeId) {
        return storeService.getProductConsumptionsByStoreId(storeId);
    }
}
