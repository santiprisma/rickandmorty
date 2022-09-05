package com.telefonica.rickandmorty.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private String name;
    private List<String> episodes;
    @JsonProperty("first_appearance")
    private String firstAppearance;
}
