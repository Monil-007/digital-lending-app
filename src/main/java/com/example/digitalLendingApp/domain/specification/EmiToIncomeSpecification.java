package com.example.digitalLendingApp.domain.specification;

import com.example.digitalLendingApp.domain.model.LoanContext;
import java.math.BigDecimal;

public class EmiToIncomeSpecification implements Specification<LoanContext> {

    @Override
    public boolean isSatisfiedBy(LoanContext context) {
        BigDecimal sixtyPercent =
                context.getMonthlyIncome().multiply(BigDecimal.valueOf(0.6));
        return context.getEmi().compareTo(sixtyPercent) <= 0;
    }

    @Override
    public String reason() {
        return "EMI_EXCEEDS_60_PERCENT";
    }
}
