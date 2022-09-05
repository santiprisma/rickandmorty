package com.telefonica.rickandmorty.service;

import com.telefonica.rickandmorty.exception.NonUniqueException;
import com.telefonica.rickandmorty.model.Character;
import com.telefonica.rickandmorty.model.CharacterSearch;
import com.telefonica.rickandmorty.model.Episode;
import com.telefonica.rickandmorty.model.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
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
        List<Episode> episodes = findEpisodes(result);

        return SearchResponse.builder()
                .name(name)
                .episodes(episodes.stream().map(Episode::getName).collect(Collectors.toList()))
                .firstAppearance(episodes.get(0).getAirDate())
                .build();
    }

    private List<Character> removeNonExpected(String expected, List<Character> characters) {
        List<Character> characterList = characters.stream()
                .filter(character -> expected.equalsIgnoreCase(character.getName()))
                .collect(Collectors.toList());

        if (characterList.isEmpty()) {
            throw new NonUniqueException(HttpStatus.BAD_REQUEST, "There are more than 1 character with this name.");
        }

        return characterList;
    }

    private List<Episode> findEpisodes(List<Character> characters) {
        List<Episode> episodes = characters.stream()
                .flatMap(character -> character.getEpisode().stream())
                .distinct()
                .map(endpoint -> apiService.executeApi(endpoint, Episode.class).block())
                .sorted(Comparator.comparing(Episode::getId))
                .collect(Collectors.toList());

        return episodes;
    }

    private String getFullEndpoint(String endpoint, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).path(endpoint).queryParams(params).build().toString();
    }
}
