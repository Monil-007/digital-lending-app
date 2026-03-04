package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.enums.EmploymentType;
import com.example.digitalLendingApp.domain.enums.RiskBand;
import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.strategy.EmploymentPremiumStrategy;
import com.example.digitalLendingApp.domain.strategy.LoanSizePremiumStrategy;
import com.example.digitalLendingApp.domain.strategy.RiskPremiumStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InterestPremiumStrategiesTest {

    @Test
    void testEmploymentPremiumStrategy() {
        EmploymentPremiumStrategy strategy = new EmploymentPremiumStrategy();
        
        LoanContext salariedContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            750,
            RiskBand.LOW
        );
        
        LoanContext selfEmployedContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SELF_EMPLOYED,
            750,
            RiskBand.LOW
        );
        
        assertEquals(BigDecimal.ZERO, strategy.premium(salariedContext));
        assertEquals(BigDecimal.ONE, strategy.premium(selfEmployedContext));
    }

    @Test
    void testLoanSizePremiumStrategy() {
        LoanSizePremiumStrategy strategy = new LoanSizePremiumStrategy();
        
        LoanContext smallLoanContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            750,
            RiskBand.LOW
        );
        
        LoanContext largeLoanContext = new LoanContext(
            BigDecimal.valueOf(1500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            750,
            RiskBand.LOW
        );
        
        assertEquals(BigDecimal.ZERO, strategy.premium(smallLoanContext));
        assertEquals(BigDecimal.valueOf(0.5), strategy.premium(largeLoanContext));
    }

    @Test
    void testRiskPremiumStrategy() {
        RiskPremiumStrategy strategy = new RiskPremiumStrategy();
        
        LoanContext lowRiskContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            800,
            RiskBand.LOW
        );
        
        LoanContext mediumRiskContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            700,
            RiskBand.MEDIUM
        );
        
        LoanContext highRiskContext = new LoanContext(
            BigDecimal.valueOf(500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SALARIED,
            620,
            RiskBand.HIGH
        );
        
        assertEquals(BigDecimal.ZERO, strategy.premium(lowRiskContext));
        assertEquals(BigDecimal.valueOf(1.5), strategy.premium(mediumRiskContext));
        assertEquals(BigDecimal.valueOf(3), strategy.premium(highRiskContext));
    }

    @Test
    void testCombinedPremiumStrategies() {
        LoanContext context = new LoanContext(
            BigDecimal.valueOf(1500000),
            36,
            30,
            BigDecimal.valueOf(50000),
            EmploymentType.SELF_EMPLOYED,
            620,
            RiskBand.HIGH
        );
        
        EmploymentPremiumStrategy employmentStrategy = new EmploymentPremiumStrategy();
        LoanSizePremiumStrategy loanSizeStrategy = new LoanSizePremiumStrategy();
        RiskPremiumStrategy riskStrategy = new RiskPremiumStrategy();
        
        BigDecimal totalPremium = BigDecimal.ZERO;
        totalPremium = totalPremium.add(employmentStrategy.premium(context));
        totalPremium = totalPremium.add(loanSizeStrategy.premium(context));
        totalPremium = totalPremium.add(riskStrategy.premium(context));
        
        assertEquals(BigDecimal.valueOf(4.5), totalPremium);
    }
}
