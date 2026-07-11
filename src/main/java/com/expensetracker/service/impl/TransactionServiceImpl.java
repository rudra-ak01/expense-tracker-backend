package com.expensetracker.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.request.TransactionRequest;
import com.expensetracker.dto.response.TransactionResponse;
import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.User;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found"));
    }

    @Override
    public TransactionResponse createTransaction(
            TransactionRequest request) {

        User currentUser = getCurrentUser();

        Transaction transaction = Transaction.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .type(request.getType())
                .description(
                        request.getDescription())
                .transactionDate(
                        request.getTransactionDate())
                .user(currentUser)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .id(savedTransaction.getId())
                .title(savedTransaction.getTitle())
                .amount(savedTransaction.getAmount())
                .type(savedTransaction.getType().name())
                .description(
                        savedTransaction.getDescription())
                .transactionDate(
                        savedTransaction.getTransactionDate())
                .build();
    }

}
