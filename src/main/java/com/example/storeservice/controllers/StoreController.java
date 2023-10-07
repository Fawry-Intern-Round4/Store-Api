package com.example.storeservice.controllers;

import com.example.storeservice.DTOs.ProductConsumptionDTO;
import com.example.storeservice.DTOs.ProductDTO;
import com.example.storeservice.DTOs.StockDTO;
import com.example.storeservice.DTOs.StoreDTO;
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

    @GetMapping("/products")
    public List<ProductDTO> getAllProductsByStoreId(@RequestParam Long storeId) {
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

    @GetMapping("/search")
    public List<ProductDTO> searchStoresByName(@RequestParam String productName) {
        return storeService.searchProductsByName(productName);
    }

    @PutMapping("/addStock")
    public StockDTO addStock(@RequestParam Long storeId, @RequestParam Long productId, @RequestParam int quantity) {
        return storeService.addStock(storeId, productId, quantity);
    }

    @PostMapping("/consumeProducts")
    public List<ProductConsumptionDTO> consumeProducts(@RequestParam Long storeId, @RequestParam Long productId, @RequestParam int quantity) {
        return storeService.consumeProducts(storeId, productId, quantity);
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
