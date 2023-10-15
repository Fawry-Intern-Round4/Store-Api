package com.example.storeservice.controllers;

import com.example.storeservice.DTOs.*;
import com.example.storeservice.DTOs.requests.OrderItemsRequest;
import com.example.storeservice.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/products")
    public List<productResponse> getAllProductsByStoreId(@RequestParam Long storeId) {
        return storeService.getAllProductsByStoreId(storeId);
    }

    @PostMapping
    public StoreDTO createStore(@RequestBody StoreDTO storeDTO) {
        return storeService.createStore(storeDTO);
    }

    @GetMapping("/get")
    public StoreDTO getStoreById(@RequestParam Long storeId) {
        return storeService.getStoreById(storeId);
    }

    @PutMapping("/addStock")
    public StockDTO addStock(@RequestBody OrderItemsRequest orderItemsRequest) {
        return storeService.addStock(orderItemsRequest);
    }

    @PostMapping("/consumeProducts")
    @ResponseStatus(code = HttpStatus.OK)
    public void consumeProducts(@RequestBody List<OrderItemsRequest> orderItemsRequests) {
        storeService.consumeProducts(orderItemsRequests);
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