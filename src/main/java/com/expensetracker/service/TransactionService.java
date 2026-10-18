package com.expensetracker.service;

import java.time.LocalDate;
import java.util.List;

import com.expensetracker.dto.request.TransactionRequest;
import com.expensetracker.dto.response.TransactionResponse;
import com.expensetracker.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);

    Page<TransactionResponse> getMyTransactions(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
    TransactionResponse updateTransaction(Long id, TransactionRequest request);

    void deleteTransaction(Long id);
}
