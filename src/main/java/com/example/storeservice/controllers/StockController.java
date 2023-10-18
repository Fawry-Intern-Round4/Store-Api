package com.example.storeservice.controllers;

import com.example.storeservice.dtos.ItemRequest;
import com.example.storeservice.dtos.StockDTO;
import com.example.storeservice.services.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public StockDTO addStock(@Valid @RequestBody ItemRequest itemRequest) {
        return stockService.addStock(itemRequest);
    }

    @PutMapping("/consumption")
    public void consumeProducts(@Valid @RequestBody List<ItemRequest> itemRequests) {
        stockService.consumeStock(itemRequests);
    }
}
