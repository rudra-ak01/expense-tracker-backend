package com.expensetracker.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponse {
    
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private String type;
    private LocalDate transactionDate;
}
