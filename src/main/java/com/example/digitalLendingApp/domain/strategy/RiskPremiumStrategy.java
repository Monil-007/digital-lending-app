package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.enums.RiskBand;
import com.example.digitalLendingApp.domain.model.LoanContext;

import java.math.BigDecimal;

public class RiskPremiumStrategy implements InterestPremiumStrategy {

    @Override
    public BigDecimal premium(LoanContext context) {
        RiskBand band = context.getRiskBand();
        return switch (band) {
            case LOW -> BigDecimal.ZERO;
            case MEDIUM -> BigDecimal.valueOf(1.5);
            case HIGH -> BigDecimal.valueOf(3);
        };
    }
}
