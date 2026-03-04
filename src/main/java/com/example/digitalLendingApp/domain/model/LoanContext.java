package com.example.digitalLendingApp.domain.model;

import com.example.digitalLendingApp.domain.enums.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanContext {

    private BigDecimal amount;
    private int tenureMonths;
    private int age;
    private BigDecimal monthlyIncome;
    private EmploymentType employmentType;
    private int creditScore;
    private RiskBand riskBand;

}
