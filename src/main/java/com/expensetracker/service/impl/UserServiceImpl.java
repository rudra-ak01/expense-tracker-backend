package com.expensetracker.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.dto.request.LoginRequest;
import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.dto.response.LoginResponse;
import com.expensetracker.dto.response.ProfileResponse;
import com.expensetracker.dto.response.UserResponse;
import com.expensetracker.entity.User;
import com.expensetracker.enums.Role;
import com.expensetracker.exception.InvalidCredentialsException;
import com.expensetracker.exception.ResourceAlreadyExistsException;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.JwtService;
import com.expensetracker.service.UserService;

@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final BCryptPasswordEncoder passwordEncoder;
        private JwtService jwtService;

        public UserServiceImpl(
                        UserRepository userRepository,
                        BCryptPasswordEncoder passwordEncoder,
                        JwtService jwtService) {

                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
        }

        
@Override
public UserResponse registerUser(RegisterRequest request) {

    // 1. Check whether email is already registered
    if (userRepository.existsByEmail(request.getEmail())) {
        throw new ResourceAlreadyExistsException("Email already exists");
    }

    // 2. Build user entity from registration request
    User user = User.builder()

            // Basic information
            .username(request.getUsername())
            .email(request.getEmail())

            // Always encrypt the password
            .password(passwordEncoder.encode(request.getPassword()))

            // Assign default role
            .role(Role.USER)

            // Personal information
            .phoneNumber(request.getPhoneNumber())
            .dateOfBirth(request.getDateOfBirth())
            .gender(request.getGender())

            // Address information
            .address(request.getAddress())
            .city(request.getCity())
            .state(request.getState())
            .country(request.getCountry())
            .postalCode(request.getPostalCode())

            // Financial preferences
            .preferredCurrency(
                request.getPreferredCurrency() == null ||
                request.getPreferredCurrency().isBlank()
                    ? "INR"
                    : request.getPreferredCurrency().toUpperCase()
            )
            .occupation(request.getOccupation())
            .monthlyIncome(request.getMonthlyIncome())

            .build();

    // 3. Save user in database
    User savedUser = userRepository.save(user);

    // 4. Return response
    return UserResponse.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .build();
}


        @Override
        public LoginResponse loginUser(LoginRequest request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

                if (!passwordMatches) {
                        throw new InvalidCredentialsException("Invalid Credentials");
                }

                String token = jwtService.generateToken(user.getEmail());

                return LoginResponse.builder()
                                .message("login successful")
                                .email(user.getEmail())
                                .token(token)
                                .build();
        }

        @Override
        public ProfileResponse getProfile() {

                String email = SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

                return ProfileResponse.builder()
                                .id(user.getId())
                                .name(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .build();
        }
}