package com.telefonica.rickandmorty.controller;

import com.telefonica.rickandmorty.model.dto.SearchResponse;
import com.telefonica.rickandmorty.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RestController {
    private final SearchService searchService;

    @GetMapping("search")
    public SearchResponse search(@RequestParam String name) {
        return searchService.searchCharacter(name);
    }
}
