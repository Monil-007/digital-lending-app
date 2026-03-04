package com.example.digitalLendingApp.domain.specification;

import com.example.digitalLendingApp.domain.model.LoanContext;

public class AgeTenureSpecification implements Specification<LoanContext> {

    @Override
    public boolean isSatisfiedBy(LoanContext context) {
        int tenureYears = context.getTenureMonths() / 12;
        return (context.getAge() + tenureYears) <= 65;
    }

    @Override
    public String reason() {
        return "AGE_TENURE_LIMIT_EXCEEDED";
    }
}
