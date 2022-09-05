package com.telefonica.rickandmorty.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {
    private String name;
    private List<String> episodes;
    @JsonProperty("first_appearance")
    private String firstAppearance;
}
