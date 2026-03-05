package com.example.digitalLendingApp.Controller.dto.Requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanApplicationRequest {

    @Valid
    @NotNull
    private ApplicantRequest applicant;

    @Valid
    @NotNull
    private LoanRequest loan;

}
