package com.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expensetracker.enums.TransactionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    
    @NotNull
    private String title;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDate transactionDate;
}
