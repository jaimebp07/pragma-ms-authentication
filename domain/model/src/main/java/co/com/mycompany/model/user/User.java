package co.com.mycompany.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import co.com.mycompany.model.authentication.Role;

public class User {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String address;
    private final String phoneNumber;
    private final String email;
    private final BigDecimal baseSalary;
    private final List<Role> roles;
    private final String password;

    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.baseSalary = builder.baseSalary;
        this.roles = List.copyOf(builder.roles);
        this.password = builder.password;
    }

    public static class Builder {
        private UUID id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String address;
        private String phoneNumber;
        private String email;
        private BigDecimal baseSalary;
        private List<Role> roles = List.of();
        private String password;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder baseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; return this; }
        public Builder roles(List<Role> roles) { this.roles = roles == null ? List.of() : List.copyOf(roles); return this; }
        public Builder password(String password) { this.password = password; return this; }

        public User build() {
            return new User(this);
        }
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public List<Role> getRoles() { return roles; }
    public String getPassword() { return password; }    

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthDate(this.birthDate)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .email(this.email)
                .roles(this.roles)
                .password(this.password)
                .baseSalary(this.baseSalary);
    }
}
