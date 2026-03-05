package com.example.digitalLendingApp.domain.specification;

import com.example.digitalLendingApp.domain.model.LoanContext;

public class CreditScoreSpecification implements Specification<LoanContext> {

    @Override
    public boolean isSatisfiedBy(LoanContext context) {
        return context.getCreditScore() >= 600;
    }

    @Override
    public String reason() {
        return "CREDIT_SCORE_BELOW_MINIMUM";
    }
}
