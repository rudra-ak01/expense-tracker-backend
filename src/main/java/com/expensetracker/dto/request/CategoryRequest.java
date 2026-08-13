package com.expensetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    
    @NotBlank(message = "Category name is Required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;
    
    @Size(max = 255, message = "Category description must not exceed 255 characters")
    private String description;
}
