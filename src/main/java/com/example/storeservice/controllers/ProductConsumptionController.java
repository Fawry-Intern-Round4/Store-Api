package com.example.storeservice.controllers;

import com.example.storeservice.dtos.ProductConsumptionDTO;
import com.example.storeservice.services.ProductConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("store/consumption")
@RequiredArgsConstructor
public class ProductConsumptionController {
    private final ProductConsumptionService productConsumptionService;

    @GetMapping
    public List<ProductConsumptionDTO> getAllProductConsumptions() {
        return productConsumptionService.getAllProductConsumptions();
    }

    @GetMapping(params = "storeId")
    public List<ProductConsumptionDTO> getProductConsumptionsByStoreId(@RequestParam Long storeId) {
        return productConsumptionService.getProductConsumptionsByStoreId(storeId);
    }
}
