package co.com.mycompany.model.user.validator;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.user.User;

import java.math.BigDecimal;

public class UserValidator {

    public static void validate(User user) {
        requireNonEmpty(user.getFirstName(), "First name is required");
        requireNonEmpty(user.getLastName(), "Last name is required");
        validateEmail(user.getEmail());
        validateSalary(user.getBaseSalary());
    }

    private static String requireNonEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(message);
        }
        return value;
    }

    private static void validateEmail(String email) {
        requireNonEmpty(email, "Email is required");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BusinessException("Email format is invalid");
        }
    }

    private static void validateSalary(BigDecimal salary) {
        if (salary == null) {
            throw new BusinessException("Salary is required");
        }
        if (salary.compareTo(BigDecimal.ZERO) < 0 || salary.compareTo(new BigDecimal("15000000")) > 0) {
            throw new BusinessException("Salary must be between 0 and 15,000,000");
        }
    }
}
