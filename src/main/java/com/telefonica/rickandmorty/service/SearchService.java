package com.telefonica.rickandmorty.service;

import com.telefonica.rickandmorty.model.Character;
import com.telefonica.rickandmorty.model.CharacterSearch;
import com.telefonica.rickandmorty.model.Episode;
import com.telefonica.rickandmorty.model.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final static String PARAM_NAME = "name";
    private final ApiService apiService;

    @Value("${rickandmorty.api.base}")
    private String baseUrl;

    @Value("${rickandmorty.api.character}")
    private String characterEndpoint;

    public SearchResponse searchCharacter(String name) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(PARAM_NAME, name);

        List<Character> result = apiService.executeApi(getFullEndpoint(characterEndpoint, params), CharacterSearch.class)
                .expand(response -> {
                    if (response.getInfo().getNext() == null) {
                        return Mono.empty();
                    }
                    return apiService.executeApi(response.getInfo().getNext(), CharacterSearch.class);
                }).flatMap(response -> Flux.fromIterable(response.getResults())).collectList().block();

        result = removeNonExpected(name, result);
        List<String> episodes = getEpisodes(result);
        Episode firstEpisode = findFirstEpisode(episodes);

        return SearchResponse.builder()
                .name(name)
                .episodes(episodes)
                .firstAppearance(firstEpisode.getAirDate())
                .build();
    }

    private List<Character> removeNonExpected(String expected, List<Character> characters) {
        return characters.stream()
                .filter(character -> expected.equalsIgnoreCase(character.getName()))
                .collect(Collectors.toList());

    }

    private List<String> getEpisodes(List<Character> characters) {
        return characters.stream()
                .flatMap(character -> character.getEpisode().stream())
                .collect(Collectors.toList());
    }

    private Episode findFirstEpisode(List<String> episodes) {
        String minEpisode = episodes.stream()
                .collect(Collectors.minBy(String.CASE_INSENSITIVE_ORDER)).orElse(null);

        return apiService.executeApi(minEpisode, Episode.class).block();
    }

    private String getFullEndpoint(String endpoint, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).path(endpoint).queryParams(params).build().toString();
    }
}
