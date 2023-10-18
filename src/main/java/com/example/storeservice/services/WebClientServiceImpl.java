package com.example.storeservice.services;

import com.example.storeservice.dtos.ProductResponse;
import com.example.storeservice.error.GeneralError;
import com.example.storeservice.error.IdsError;
import com.example.storeservice.exception.IdsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class WebClientServiceImpl implements WebClientService {
    private final WebClient.Builder webClient;

    @Override
    public List<ProductResponse> getProducts(Set<Long> productIds) {
        return webClient.build()
                .get()
                .uri("lb://product-api/product", uriBuilder ->
                        uriBuilder.queryParam("ids", productIds).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse
                                .bodyToMono(IdsError.class)
                                .flatMap(idsError ->
                                        Mono.error(new IdsException(idsError.getMessage(), idsError.getIds()))
                                )
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse
                                .bodyToMono(String.class)
                                .flatMap(error ->
                                        Mono.error(new RuntimeException(error))
                                )
                )
                .bodyToFlux(ProductResponse.class)
                .collectList()
                .block();
    }

    @Override
    public void checkIfProductExist(Long productId) {
        webClient.build()
                .get()
                .uri("lb://product-api/product/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse
                                .bodyToMono(GeneralError.class)
                                .flatMap(generalError ->
                                        Mono.error(new IllegalArgumentException(generalError.getMessage()))
                                )
                )
                .toEntity(String.class)
                .block();
    }

}
