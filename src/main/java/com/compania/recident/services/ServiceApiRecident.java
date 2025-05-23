package com.compania.recident.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compania.recident.models.entities.Recident;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ServiceApiRecident {
    
    private RestTemplate restTemplate;

    public ServiceApiRecident(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    
    public Recident[] getAllRecident(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
					"https://rickandmortyapi.com/api/location/",String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode resultsNode = objectMapper.readTree(responseEntity.getBody()).get("results");
            Recident[] recidents = objectMapper.readValue(resultsNode.toString(), Recident[].class);
            return recidents;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
