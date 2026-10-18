package com.expensetracker.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.expensetracker.entity.Category;
import com.expensetracker.enums.TransactionType;
import com.expensetracker.exception.CategoryNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        private final CategoryRepository categoryRepository;

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

            Category category = categoryRepository
                    .findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() ->
                            new CategoryNotFoundException("Category not found"));

                Transaction transaction = Transaction.builder()
                                .title(request.getTitle())
                                .amount(request.getAmount())
                                .type(request.getType())
                                .description(request.getDescription())
                                .transactionDate(request.getTransactionDate())
                                .user(currentUser)
                                .category(category)
                                .build();

                Transaction saved = transactionRepository.save(transaction);

                return mapToResponse(saved);
        }

    @Override
    public Page<TransactionResponse> getMyTransactions(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date must not be after end date");
        }

        User currentUser = getCurrentUser();

        Page<Transaction> transactions =
                transactionRepository.searchTransactions(
                        currentUser,
                        type,
                        startDate,
                        endDate,
                        pageable);

        return transactions.map(this::mapToResponse);
    }

        @Override
        public TransactionResponse updateTransaction(Long id, TransactionRequest request) {

                User currentUser = getCurrentUser();

            Category category = categoryRepository
                    .findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() ->
                            new CategoryNotFoundException("Category not found"));

                Transaction transaction = transactionRepository
                                .findByIdAndUser(id, currentUser)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                transaction.setTitle(request.getTitle());
                transaction.setAmount(request.getAmount());
                transaction.setType(request.getType());
                transaction.setDescription(request.getDescription());
                transaction.setTransactionDate(request.getTransactionDate());
                transaction.setCategory(category);
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
                                .categoryId(transaction.getCategory().getId())
                                .categoryName(transaction.getCategory().getName())
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
