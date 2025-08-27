package co.com.mycompany.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User {
    String id;  
    String firstName;  
    String lastName;  
    LocalDate birthDate;  
    String address;  
    String phoneNumber;  
    String email;  
    BigDecimal baseSalary;

    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = requireNonEmpty(builder.firstName, "First name is required");
        this.lastName = requireNonEmpty(builder.lastName, "Last name is required");
        this.birthDate = builder.birthDate;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = validateEmail(builder.email);
        this.baseSalary = validateSalary(builder.baseSalary);
    }

    private static String requireNonEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private static String validateEmail(String email) {
        requireNonEmpty(email, "Email is required");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        return email;
    }

    private static BigDecimal validateSalary(BigDecimal salary) {
        if (salary == null) {
            throw new IllegalArgumentException("Salary is required");
        }
        if (salary.compareTo(BigDecimal.ZERO) < 0 || salary.compareTo(new BigDecimal("15000000")) > 0) {
            throw new IllegalArgumentException("Salary must be between 0 and 15,000,000");
        }
        return salary;
    }

    public static class Builder {
        private String id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String address;
        private String phoneNumber;
        private String email;
        private BigDecimal baseSalary;

        public Builder id(String id) { this.id = id; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder baseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; return this; }

        public User build() {
            return new User(this);
        }
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public BigDecimal getBaseSalary() { return baseSalary; }

}
