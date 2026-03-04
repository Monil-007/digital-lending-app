package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.model.LoanContext;

import java.math.BigDecimal;

public class EmploymentPremiumStrategy implements InterestPremiumStrategy {

    @Override
    public BigDecimal premium(LoanContext context) {
        return context.getEmploymentType().name().equals("SELF_EMPLOYED")
                ? BigDecimal.ONE
                : BigDecimal.ZERO;
    }
}
