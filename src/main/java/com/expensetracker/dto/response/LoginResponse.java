package com.expensetracker.dto.response;

import lombok.*;

@Getter
@Builder
public class LoginResponse {
    
    private String message;
    private String email;
    private String token;
}
