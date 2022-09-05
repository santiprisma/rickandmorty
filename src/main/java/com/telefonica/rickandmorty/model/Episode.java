package com.telefonica.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {
    private Integer id;
    private String name;
    @JsonProperty("air_date")
    private String airDate;
    private String episode;
}
