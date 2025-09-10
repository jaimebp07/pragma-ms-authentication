package co.com.mycompany.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull LocalDate birthDate,
    String address,
    @Pattern(regexp = "\\d{10}") String phoneNumber,
    @Email String email,
    @DecimalMin("0.0") @DecimalMax("15000000.0") BigDecimal baseSalary,
    @NotNull @Size(min = 1) List<String> roles,
    @NotBlank @Size(min = 8, max = 255) String password
) {}
