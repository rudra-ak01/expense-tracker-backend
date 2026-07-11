package com.expensetracker.service;

import com.expensetracker.dto.request.TransactionRequest;
import com.expensetracker.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
}
