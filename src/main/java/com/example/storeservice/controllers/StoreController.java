package com.example.storeservice.controllers;

import com.example.storeservice.dtos.StoreDTO;
import com.example.storeservice.dtos.ProductResponse;
import com.example.storeservice.services.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public StoreDTO createStore(@Valid @RequestBody StoreDTO storeDTO) {
        return storeService.createStore(storeDTO);
    }

    @GetMapping
    public List<StoreDTO> getAllStores() {
        return storeService.getAllStores();
    }

    @GetMapping("{id}")
    public StoreDTO getStoreById(@PathVariable("id") Long id) {
        return storeService.getStoreById(id);
    }

    @GetMapping("{id}/product")
    public List<ProductResponse> getAllProductsByStoreId(@PathVariable("id") Long id) {
        return storeService.getAllProductsByStoreId(id);
    }

}
