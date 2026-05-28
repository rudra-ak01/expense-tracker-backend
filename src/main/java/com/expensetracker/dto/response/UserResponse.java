package com.expensetracker.dto.response;

import lombok.*;

@Getter
@Builder
public class UserResponse {
    
    private Long id;

    private String username;

    private String email;
    
}
