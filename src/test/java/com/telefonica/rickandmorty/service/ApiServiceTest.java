package com.telefonica.rickandmorty.service;

import com.telefonica.rickandmorty.model.CharacterSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {
    @InjectMocks
    private ApiService service;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void init() {
        given(webClient.method(any())).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.uri(anyString())).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.onStatus(any(), any())).willReturn(responseSpec);
    }

    @Test
    public void givenUrl_whenExecuteApi_thenReturnOk() throws Exception {
        given(responseSpec.bodyToMono(CharacterSearch.class)).willReturn(Mono.just(new CharacterSearch()));
        Mono<CharacterSearch> result = service.executeApi("http://www.google.com.ar", CharacterSearch.class);

        assertNotNull(result.block());
    }
}