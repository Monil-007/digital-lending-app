package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.model.LoanContext;

import java.math.BigDecimal;

public interface InterestPremiumStrategy {
    BigDecimal premium(LoanContext context);
}
