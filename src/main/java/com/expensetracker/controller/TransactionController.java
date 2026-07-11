package com.expensetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.dto.request.TransactionRequest;
import com.expensetracker.dto.response.TransactionResponse;
import com.expensetracker.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        transactionService
                                .createTransaction(
                                        request));
    }
}
