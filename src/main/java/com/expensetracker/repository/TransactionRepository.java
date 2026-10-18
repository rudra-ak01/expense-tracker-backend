package com.expensetracker.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expensetracker.entity.Category;
import com.expensetracker.entity.Transaction;
import com.expensetracker.entity.User;
import com.expensetracker.enums.TransactionType;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndUser(Long id, User user);

    boolean existsByCategory(Category category);

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.user = :user
        AND (:type IS NULL OR t.type = :type)
        AND (:startDate IS NULL OR t.transactionDate >= :startDate)
        AND (:endDate IS NULL OR t.transactionDate <= :endDate)
        """)
    Page<Transaction> searchTransactions(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.user = :user
      AND t.type = :type
    """)
    BigDecimal calculateTotalByUserAndType(
            @Param("user") User user,
            @Param("type") TransactionType type);


    @Query("""
    SELECT c.id, c.name, COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    JOIN t.category c
    WHERE t.user = :user
      AND t.type = :type
    GROUP BY c.id, c.name
    ORDER BY SUM(t.amount) DESC
    """)
    List<Object[]> calculateCategoryTotals(
            @Param("user") User user,
            @Param("type") TransactionType type);
}