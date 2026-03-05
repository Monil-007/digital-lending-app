package com.example.digitalLendingApp.Controller.dto.Requests;

import com.example.digitalLendingApp.domain.enums.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ApplicantRequest {

    @NotBlank(message = "Applicant name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 21, message = "Age must be at least 21")
    @Max(value = 60, message = "Age must be less than or equal to 60")
    private Integer age;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.01", message = "Monthly income must be greater than zero")
    private Double monthlyIncome;

    @NotNull(message = "Employment type must be provided")
    private EmploymentType employmentType;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 900, message = "Credit score must be less than or equal to 900")
    private Integer creditScore;

}
