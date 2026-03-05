package com.example.digitalLendingApp.domain.specification;

import com.example.digitalLendingApp.domain.model.LoanContext;
import java.util.ArrayList;
import java.util.List;

public class EligibilityEvaluator {

    private final List<Specification<LoanContext>> specifications;

    public EligibilityEvaluator(List<Specification<LoanContext>> specifications) {
        this.specifications = specifications;
    }

    public List<String> evaluate(LoanContext context) {
        List<String> failures = new ArrayList<>();
        for (Specification<LoanContext> spec : specifications) {
            if (!spec.isSatisfiedBy(context)) {
                failures.add(spec.reason());
            }
        }
        return failures;
    }
}
