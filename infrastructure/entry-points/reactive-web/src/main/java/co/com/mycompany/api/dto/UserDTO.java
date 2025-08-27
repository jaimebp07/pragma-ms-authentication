package co.com.mycompany.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserDTO(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull LocalDate birthDate,
    String address,
    @Pattern(regexp = "\\d{10}") String phoneNumber,
    @Email String email,
    @DecimalMin("0.0") @DecimalMax("15000000.0") BigDecimal baseSalary
) {}
