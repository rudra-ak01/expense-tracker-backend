package com.expensetracker.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndUser(Long id, User user);

    boolean existsByNameAndUser(String name, User user);

    List<Category> findByUser(User user);

    boolean existsByNameAndUserAndIdNot(String name, User user, Long id);
}
