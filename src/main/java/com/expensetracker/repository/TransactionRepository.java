package com.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.User;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
