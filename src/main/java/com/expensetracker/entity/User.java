package com.expensetracker.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.expensetracker.enums.Role;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic account information
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Personal information
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String gender;

    // Address information
    @Column(length = 500)
    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    // Financial preferences
    @Builder.Default
    @Column(nullable = false, length = 3)
    private String preferredCurrency = "INR";

    private String occupation;

    private BigDecimal monthlyIncome;

    // Relationships
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

}
