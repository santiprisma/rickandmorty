package com.telefonica.rickandmorty.model;

import lombok.Data;

@Data
public class Info {
    private Integer count;
    private String pages;
    private String next;
    private String prev;
}
