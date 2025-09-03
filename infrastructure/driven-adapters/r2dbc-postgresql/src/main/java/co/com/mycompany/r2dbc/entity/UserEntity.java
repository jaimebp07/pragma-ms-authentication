package co.com.mycompany.r2dbc.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Table("credi_ya.users")
public class UserEntity {

    @Id
    @Column("id")
    private UUID id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("address")
    private String address;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("base_salary")
    private BigDecimal baseSalary;
    
}
