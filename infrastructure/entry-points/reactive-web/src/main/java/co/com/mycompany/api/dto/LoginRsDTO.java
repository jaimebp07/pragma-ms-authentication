package co.com.mycompany.api.dto;

public record LoginRsDTO(
    String token, 
    String tokenType, 
    String expiresAt
) { }
