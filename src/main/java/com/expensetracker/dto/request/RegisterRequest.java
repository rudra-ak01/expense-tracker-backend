
package com.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    // Basic information
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
          message = "Password must be between 8 and 100 characters")
    private String password;

    // Personal information
    @Size(max = 20)
    private String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String gender;

    // Address information
    @Size(max = 500)
    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    // Financial preferences
    @Pattern(regexp = "^[A-Za-z]{3}$",
             message = "Currency must be a valid 3-letter code")
    private String preferredCurrency = "INR";

    private String occupation;

    @PositiveOrZero(message = "Monthly income cannot be negative")
    private BigDecimal monthlyIncome;
}
