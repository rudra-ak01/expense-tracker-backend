package com.expensetracker.service;

import com.expensetracker.dto.request.RegisterRequest;
import com.expensetracker.dto.response.UserResponse;

public interface UserService {
   UserResponse registerUser(RegisterRequest request);
}
