package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.specification.EmiToIncomeSpecification;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EmiToIncomeSpecificationTest {

    @Test
    void testEmiWithinSixtyPercent() {
        EmiToIncomeSpecification spec = new EmiToIncomeSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        
        // EMI of 25,000 is 50% of 50,000 income
        context.setEmi(BigDecimal.valueOf(25000));

        assertTrue(spec.isSatisfiedBy(context));
    }

    @Test
    void testEmiAtSixtyPercent() {
        EmiToIncomeSpecification spec = new EmiToIncomeSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        
        // EMI of 30,000 is exactly 60% of 50,000 income
        context.setEmi(BigDecimal.valueOf(30000));

        assertTrue(spec.isSatisfiedBy(context));
    }

    @Test
    void testEmiExceedsSixtyPercent() {
        EmiToIncomeSpecification spec = new EmiToIncomeSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        
        // EMI of 35,000 is 70% of 50,000 income
        context.setEmi(BigDecimal.valueOf(35000));

        assertFalse(spec.isSatisfiedBy(context));
        assertEquals("EMI_EXCEEDS_60_PERCENT", spec.reason());
    }

    @Test
    void testLowIncomeHighEmi() {
        EmiToIncomeSpecification spec = new EmiToIncomeSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(300000),
                24,
                25,
                BigDecimal.valueOf(20000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                650,
                com.example.digitalLendingApp.domain.enums.RiskBand.MEDIUM
        );
        
        // EMI of 15,000 is 75% of 20,000 income
        context.setEmi(BigDecimal.valueOf(15000));

        assertFalse(spec.isSatisfiedBy(context));
        assertEquals("EMI_EXCEEDS_60_PERCENT", spec.reason());
    }

    @Test
    void testHighIncomeLowEmi() {
        EmiToIncomeSpecification spec = new EmiToIncomeSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(1000000),
                60,
                35,
                BigDecimal.valueOf(200000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                800,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );
        
        // EMI of 80,000 is 40% of 200,000 income
        context.setEmi(BigDecimal.valueOf(80000));

        assertTrue(spec.isSatisfiedBy(context));
    }
}
