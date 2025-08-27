package co.com.mycompany.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private String id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private Double baseSalary;
}
