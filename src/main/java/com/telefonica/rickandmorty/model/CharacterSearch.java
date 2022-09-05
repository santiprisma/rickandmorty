package com.telefonica.rickandmorty.model;

import lombok.Data;

import java.util.List;

@Data
public class CharacterSearch {
    private Info info;
    private List<Character> results;
}
