package com.example.digitalLendingApp.Controller.dto.Requests;

import com.example.digitalLendingApp.domain.enums.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ApplicantRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(21)
    @Max(60)
    private Integer age;

    @NotNull
    @DecimalMin("0.01")
    private Double monthlyIncome;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    @Min(300)
    @Max(900)
    private Integer creditScore;

}
