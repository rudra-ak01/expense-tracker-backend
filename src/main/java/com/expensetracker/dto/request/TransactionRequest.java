package com.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expensetracker.enums.TransactionType;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotNull(message = "Date is required")
    private LocalDate transactionDate;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
