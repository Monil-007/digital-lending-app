package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.specification.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EligibilityEvaluatorTest {

    @Test
    void testAllSpecificationsSatisfied() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        context.setEmi(BigDecimal.valueOf(25000));

        List<String> failures = evaluator.evaluate(context);
        assertTrue(failures.isEmpty());
    }

    @Test
    void testCreditScoreFailure() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                550, // Low credit score
                com.example.digitalLendingApp.domain.enums.RiskBand.HIGH
        );
        context.setEmi(BigDecimal.valueOf(25000));

        List<String> failures = evaluator.evaluate(context);
        assertEquals(1, failures.size());
        assertTrue(failures.contains("CREDIT_SCORE_BELOW_MINIMUM"));
    }

    @Test
    void testAgeTenureFailure() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                480, // 40 years tenure
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        context.setEmi(BigDecimal.valueOf(25000));

        List<String> failures = evaluator.evaluate(context);
        assertEquals(1, failures.size());
        assertTrue(failures.contains("AGE_TENURE_LIMIT_EXCEEDED"));
    }

    @Test
    void testEmiToIncomeFailure() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        context.setEmi(BigDecimal.valueOf(35000)); // 70% of income

        List<String> failures = evaluator.evaluate(context);
        assertEquals(1, failures.size());
        assertTrue(failures.contains("EMI_EXCEEDS_60_PERCENT"));
    }

    @Test
    void testMultipleFailures() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                480, // 40 years tenure
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                550, // Low credit score
                com.example.digitalLendingApp.domain.enums.RiskBand.HIGH
        );
        context.setEmi(BigDecimal.valueOf(35000)); // 70% of income

        List<String> failures = evaluator.evaluate(context);
        assertEquals(3, failures.size());
        assertTrue(failures.contains("CREDIT_SCORE_BELOW_MINIMUM"));
        assertTrue(failures.contains("AGE_TENURE_LIMIT_EXCEEDED"));
        assertTrue(failures.contains("EMI_EXCEEDS_60_PERCENT"));
    }

    @Test
    void testPartialFailures() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                480, // 40 years tenure
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750, // Good credit score
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        context.setEmi(BigDecimal.valueOf(35000)); // 70% of income

        List<String> failures = evaluator.evaluate(context);
        assertEquals(2, failures.size());
        assertTrue(failures.contains("AGE_TENURE_LIMIT_EXCEEDED"));
        assertTrue(failures.contains("EMI_EXCEEDS_60_PERCENT"));
        assertFalse(failures.contains("CREDIT_SCORE_BELOW_MINIMUM"));
    }

    @Test
    void testEmptySpecificationsList() {
        EligibilityEvaluator evaluator = new EligibilityEvaluator(List.of());

        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        List<String> failures = evaluator.evaluate(context);
        assertTrue(failures.isEmpty());
    }
}
