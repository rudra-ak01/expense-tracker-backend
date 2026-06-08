package com.expensetracker.service;

import com.expensetracker.dto.request.LoginRequest;
import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.dto.response.LoginResponse;
import com.expensetracker.dto.response.ProfileResponse;
import com.expensetracker.dto.response.UserResponse;

public interface UserService {
   UserResponse registerUser(RegisterRequest request);
   LoginResponse loginUser(LoginRequest request);
   ProfileResponse getProfile();
}
