package com.telefonica.rickandmorty.model;

import lombok.Data;

import java.util.List;

@Data
public class Character {
    private Integer id;
    private String name;
    private String status;
    private List<String> episode;
}
