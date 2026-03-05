package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.model.LoanContext;
import java.math.BigDecimal;

public class LoanSizePremiumStrategy implements InterestPremiumStrategy {

    @Override
    public BigDecimal premium(LoanContext context) {
        return context.getAmount().compareTo(BigDecimal.valueOf(1000000)) > 0
                ? BigDecimal.valueOf(0.5)
                : BigDecimal.ZERO;
    }
}
