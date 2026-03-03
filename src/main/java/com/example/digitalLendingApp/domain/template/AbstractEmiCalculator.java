package com.example.digitalLendingApp.domain.template;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractEmiCalculator {

    public BigDecimal calculate(BigDecimal principal,
                                BigDecimal annualRate,
                                int tenureMonths) {

        BigDecimal monthlyRate =
                annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        return compute(principal, monthlyRate, tenureMonths)
                .setScale(2, RoundingMode.HALF_UP);
    }

    protected abstract BigDecimal compute(BigDecimal p,
                                          BigDecimal r,
                                          int n);
}
