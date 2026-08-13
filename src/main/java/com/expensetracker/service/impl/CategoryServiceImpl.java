package com.expensetracker.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.expensetracker.dto.request.CategoryRequest;
import com.expensetracker.dto.response.CategoryResponse;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.exception.CategoryInUseException;
import com.expensetracker.exception.CategoryNotFoundException;
import com.expensetracker.exception.DuplicateCategoryException;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.TransactionRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

        private CategoryRepository categoryRepository;
        private UserRepository userRepository;
        private TransactionRepository transactionRepository;

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
        @Transactional
        public CategoryResponse createCategory(CategoryRequest categoryRequest) {
                User currentUser = getCurrentUser();

                if (categoryRepository.existsByNameAndUser(
                                categoryRequest.getName(),
                                currentUser)) {

                        throw new DuplicateCategoryException(
                                        "Category already exists.");
                }

                Category category = Category.builder()
                                .name(categoryRequest.getName())
                                .description(categoryRequest.getDescription())
                                .user(currentUser)
                                .build();

                Category savedCategory = categoryRepository.save(category);
                return CategoryResponse.builder()
                                .id(savedCategory.getId())
                                .name(savedCategory.getName())
                                .description(savedCategory.getDescription())
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public List<CategoryResponse> getMyCategories() {
                User currentUser = getCurrentUser();

                List<Category> categories = categoryRepository.findByUser(currentUser);

                return categories.stream()
                                .map(category -> CategoryResponse.builder()
                                                .id(category.getId())
                                                .name(category.getName())
                                                .description(category.getDescription())
                                                .build())
                                .toList();
        }

        @Override
        @Transactional
        public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
                User currentUser = getCurrentUser();

                Category category = categoryRepository.findByIdAndUser(id, currentUser)
                                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

                if (categoryRepository.existsByNameAndUserAndIdNot(categoryRequest.getName(), currentUser, id)) {
                        throw new DuplicateCategoryException("Category already exists");
                }

                category.setName(categoryRequest.getName());
                category.setDescription(categoryRequest.getDescription());

                return CategoryResponse.builder()
                                .id(category.getId())
                                .description(category.getDescription())
                                .name(category.getName())
                                .build();
        }

        @Override
        @Transactional
        public void deleteCategory(Long id) {
                User currentUser = getCurrentUser();

                Category category = categoryRepository.findByIdAndUser(id, currentUser)
                                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

                if (transactionRepository.existsByCategory(category)){
                        throw new CategoryInUseException("Category is in use and cannot be deleted.");
                }

                categoryRepository.delete(category);
        }

}
