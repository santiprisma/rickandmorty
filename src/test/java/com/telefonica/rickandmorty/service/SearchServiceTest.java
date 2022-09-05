package com.telefonica.rickandmorty.service;

import com.telefonica.rickandmorty.exception.NonUniqueException;
import com.telefonica.rickandmorty.model.Character;
import com.telefonica.rickandmorty.model.CharacterSearch;
import com.telefonica.rickandmorty.model.Episode;
import com.telefonica.rickandmorty.model.Info;
import com.telefonica.rickandmorty.model.dto.SearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @InjectMocks
    private SearchService service;

    @Mock
    private ApiService apiService;

    private final static List<Episode> episodeList = Arrays.asList(
            new Episode(6, "Episode 6", "December 6, 2013", "S06E01"),
            new Episode(62, "Episode 62", "December 15, 2015", "S62E01"),
            new Episode(1, "Episode 1", "December 1, 2013", "S01E01"),
            new Episode(2, "Episode 2", "December 2, 2013", "S02E01"),
            new Episode(35, "Episode 35", "January 12, 2014", "S35E01"),
            new Episode(10, "Episode 10", "December 10, 2013", "S10E01"),
            new Episode(3, "Episode 3", "December 3, 2013", "S03E01")
    );

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(service, "baseUrl", "https://rickandmortyapi.com");
        ReflectionTestUtils.setField(service, "characterEndpoint", "/character");
    }

    @Test
    public void givenValidName_whenSearchCharacter_thenReturnOk() throws Exception {
        CharacterSearch mockCharacters = mockCharacters();
        given(apiService.executeApi(anyString(), eq(CharacterSearch.class))).willReturn(Mono.just(mockCharacters));
        mockEpisodes();

        SearchResponse response = service.searchCharacter("Rick Sanchez");
        assertEquals("Rick Sanchez", response.getName());
        assertEquals(7, response.getEpisodes().size());
        assertEquals("December 1, 2013", response.getFirstAppearance());

        verify(apiService, times(8)).executeApi(any(), any());
    }

    @Test
    public void givenNonUniqueName_whenSearchCharacter_thenThrowsNonUniqueException() throws Exception {
        CharacterSearch mockCharacters = mockCharacters();
        Character character = new Character();
        character.setId(2);
        character.setName("Rick Gonzalez");
        character.setStatus("Alive");
        mockCharacters.getResults().add(character);

        given(apiService.executeApi(anyString(), eq(CharacterSearch.class))).willReturn(Mono.just(mockCharacters));

        assertThrows(NonUniqueException.class, () -> service.searchCharacter("Rick"));
        verify(apiService, times(1)).executeApi(any(), any());
    }

    private CharacterSearch mockCharacters() {
        CharacterSearch characterSearch = new CharacterSearch();
        Info info = new Info();

        List<Character> characterList = new ArrayList<>();
        Character character = new Character();
        character.setId(1);
        character.setName("Rick Sanchez");
        character.setStatus("Alive");
        character.setEpisode(Arrays.asList("https://rickandmortyapi.com/api/episode/6", "https://rickandmortyapi.com/api/episode/62", "https://rickandmortyapi.com/api/episode/1", "https://rickandmortyapi.com/api/episode/2"));
        characterList.add(character);

        character = new Character();
        character.setId(2);
        character.setName("Rick Sanchez");
        character.setStatus("Dead");
        character.setEpisode(Arrays.asList("https://rickandmortyapi.com/api/episode/3", "https://rickandmortyapi.com/api/episode/35", "https://rickandmortyapi.com/api/episode/10", "https://rickandmortyapi.com/api/episode/1"));
        characterList.add(character);

        info.setCount(characterList.size());
        info.setPages("1");

        characterSearch.setResults(characterList);
        characterSearch.setInfo(info);

        return characterSearch;
    }

    private void mockEpisodes() {
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/6", Episode.class)).willReturn(Mono.just(episodeList.get(0)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/62", Episode.class)).willReturn(Mono.just(episodeList.get(1)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/1", Episode.class)).willReturn(Mono.just(episodeList.get(2)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/2", Episode.class)).willReturn(Mono.just(episodeList.get(3)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/35", Episode.class)).willReturn(Mono.just(episodeList.get(4)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/10", Episode.class)).willReturn(Mono.just(episodeList.get(5)));
        given(apiService.executeApi("https://rickandmortyapi.com/api/episode/3", Episode.class)).willReturn(Mono.just(episodeList.get(5)));
    }
}