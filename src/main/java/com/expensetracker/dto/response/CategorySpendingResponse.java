package com.expensetracker.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorySpendingResponse {
    private Long categoryId;
    private String categoryName;
    private BigDecimal totalBalance;
}
