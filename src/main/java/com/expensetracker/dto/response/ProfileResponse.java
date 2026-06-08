package com.expensetracker.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    
    private Long id;
    private String name;
    private String email;
    private String role;
}
