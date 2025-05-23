package com.compania.recident.models.entities;

import lombok.Data;

@Data
public class Recident {

    private int id;
    private String name;
    private String type;
    private String dimension;
    private String[] residents;
    private String url;
    private String created;

}