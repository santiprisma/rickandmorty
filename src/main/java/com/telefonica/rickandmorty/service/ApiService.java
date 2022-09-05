package com.telefonica.rickandmorty.service;

import com.telefonica.rickandmorty.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {
    private final WebClient webClient;

    public <O> Mono<O> executeApi(String endpoint, Class<O> outputClass) {
        log.info("[executeApi] Executing {}. Expecting result: {}", endpoint, outputClass);
        return webClient
                .method(HttpMethod.GET)
                .uri(endpoint)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> Mono.error(new ApiException(clientResponse.statusCode(), clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(outputClass);
    }
}
