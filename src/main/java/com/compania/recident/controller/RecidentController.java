package com.compania.recident.controller;

import org.springframework.web.bind.annotation.RestController;

import com.compania.recident.models.entities.Recident;
import com.compania.recident.services.ServiceApiRecident;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Configuration
public class RecidentController {

    private ServiceApiRecident serviceApiRecident;

    RecidentController(ServiceApiRecident serviceApiRecident){
        this.serviceApiRecident = serviceApiRecident;
    }

    @GetMapping("recidents")
    private Recident[] getRecident(){
        return this.serviceApiRecident.getAllRecident();
    } 
}
