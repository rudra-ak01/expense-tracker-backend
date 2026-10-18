package com.expensetracker.service.impl;

import com.expensetracker.dto.response.CategorySpendingResponse;
import com.expensetracker.dto.response.DashboardResponse;
import com.expensetracker.entity.User;
import com.expensetracker.enums.TransactionType;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        BigDecimal totalIncome =
                transactionRepository.calculateTotalByUserAndType(
                        currentUser, TransactionType.INCOME);

        BigDecimal totalExpenses =
                transactionRepository.calculateTotalByUserAndType(
                        currentUser, TransactionType.EXPENSE);

        BigDecimal balance = totalIncome.subtract(totalExpenses);

        List<CategorySpendingResponse> categorySpending =
                transactionRepository.calculateCategoryTotals(
                                currentUser, TransactionType.EXPENSE)
                        .stream()
                        .map(row -> CategorySpendingResponse.builder()
                                .categoryId((Long) row[0])
                                .categoryName((String) row[1])
                                .totalBalance((BigDecimal) row[2])
                                .build())
                        .toList();

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpenses)
                .balance(balance)
                .categorySpending(categorySpending)
                .build();
    }
}
