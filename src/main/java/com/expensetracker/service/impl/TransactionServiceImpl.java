package com.expensetracker.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.request.TransactionRequest;
import com.expensetracker.dto.response.TransactionResponse;
import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.User;
import com.expensetracker.exception.TransactionNotFoundException;
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

                Transaction saved = transactionRepository.save(transaction);

                return mapToResponse(saved);
        }

        @Override
        public List<TransactionResponse> getMyTransaction() {

                User currentUser = getCurrentUser();

                List<Transaction> transactions = transactionRepository.findByUser(currentUser);

                return transactions.stream()
                                .map(this::mapToResponse)
                                .toList();

        }

        @Override
        public TransactionResponse updateTransaction(Long id, TransactionRequest request) {

                User currentUser = getCurrentUser();

                Transaction transaction = transactionRepository
                                .findByIdAndUser(id, currentUser)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                transaction.setTitle(request.getTitle());
                transaction.setAmount(request.getAmount());
                transaction.setType(request.getType());
                transaction.setDescription(request.getDescription());
                transaction.setTransactionDate(request.getTransactionDate());
                Transaction updated = transactionRepository.save(transaction);

                return mapToResponse(updated);
        }

        private TransactionResponse mapToResponse(Transaction transaction) {
                return TransactionResponse.builder()
                                .id(transaction.getId())
                                .title(transaction.getTitle())
                                .amount(transaction.getAmount())
                                .type(transaction.getType().name())
                                .description(transaction.getDescription())
                                .transactionDate(transaction.getTransactionDate())
                                .build();
        }

        @Override
        public void deleteTransaction(Long id) {
                User currentUser = getCurrentUser();

                Transaction transaction = transactionRepository.findByIdAndUser(id, currentUser)
                                .orElseThrow(()-> new TransactionNotFoundException("Transaction Not Found"));

                transactionRepository.delete(transaction);
        }
}
