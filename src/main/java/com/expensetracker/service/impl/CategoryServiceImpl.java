package com.expensetracker.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

@Service
public class CategoryServiceImpl implements CategoryService {

        private final CategoryRepository categoryRepository;
        private final UserRepository userRepository;
        private final TransactionRepository transactionRepository;

         public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository) {

        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

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
                return mapToResponse(savedCategory);
        }

        @Override
        @Transactional(readOnly = true)
        public List<CategoryResponse> getMyCategories() {
                User currentUser = getCurrentUser();

                List<Category> categories = categoryRepository.findByUser(currentUser);

                return categories.stream()
                                .map(this::mapToResponse)
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

                return mapToResponse(category);
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

        private CategoryResponse mapToResponse(Category category){
                return CategoryResponse.builder()
                                .id(category.getId())
                                .description(category.getDescription())
                                .name(category.getName())
                                .build();
        }

}
