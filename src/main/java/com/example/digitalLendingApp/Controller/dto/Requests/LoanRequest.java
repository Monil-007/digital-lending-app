package com.example.digitalLendingApp.Controller.dto.Requests;

import com.example.digitalLendingApp.domain.enums.LoanPurpose;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoanRequest {

    @NotNull(message = "Loan amount is required")
    @Min(value = 10000, message = "Minimum loan amount is 10,000")
    @Max(value = 5000000, message = "Maximum loan amount is 50,00,000")
    private Double amount;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Minimum tenure is 6 months")
    @Max(value = 360, message = "Maximum tenure is 360 months")
    private Integer tenureMonths;

    @NotNull(message = "Loan purpose must be specified")
    private LoanPurpose purpose;

}
