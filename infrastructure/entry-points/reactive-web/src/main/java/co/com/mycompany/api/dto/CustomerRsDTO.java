package co.com.mycompany.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CustomerRsDTO(
    UUID id,
    String firstName,
    String lastName,
    String email,
    BigDecimal baseSalary
) { }
