package co.com.mycompany.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO(
    String id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String address,
    String phoneNumber,
    String email,
    BigDecimal baseSalary
) {}
