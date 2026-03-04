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
    private BigDecimal emi;

    public LoanContext(BigDecimal amount,
                       int tenureMonths,
                       int age,
                       BigDecimal monthlyIncome,
                       EmploymentType employmentType,
                       int creditScore,
                       RiskBand riskBand) {

        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.employmentType = employmentType;
        this.creditScore = creditScore;
        this.riskBand = riskBand;
    }

}
