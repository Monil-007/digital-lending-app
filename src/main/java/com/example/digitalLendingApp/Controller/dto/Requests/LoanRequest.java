package com.example.digitalLendingApp.Controller.dto.Requests;

import com.example.digitalLendingApp.domain.enums.LoanPurpose;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoanRequest {

    @NotNull
    @Min(10000)
    @Max(5000000)
    private Double amount;

    @NotNull
    @Min(6)
    @Max(360)
    private Integer tenureMonths;

    @NotNull
    private LoanPurpose purpose;

}
