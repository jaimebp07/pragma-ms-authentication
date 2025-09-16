package co.com.mycompany.api.dto;

import java.math.BigDecimal;

public record CustomerRsDTO(
    String firstName,
    String lastName,
    String email,
    BigDecimal baseSalary
) { }
