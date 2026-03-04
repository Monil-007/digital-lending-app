package com.example.digitalLendingApp;

import com.example.digitalLendingApp.Controller.dto.Requests.LoanApplicationRequest;
import com.example.digitalLendingApp.Controller.dto.Requests.ApplicantRequest;
import com.example.digitalLendingApp.Controller.dto.Requests.LoanRequest;
import com.example.digitalLendingApp.Controller.dto.Responses.LoanApplicationResponse;
import com.example.digitalLendingApp.domain.enums.EmploymentType;
import com.example.digitalLendingApp.repository.InMemoryLoanApplicationRepository;
import com.example.digitalLendingApp.service.LoanApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationServiceEligibilityTest {

    private LoanApplicationService service;
    private InMemoryLoanApplicationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLoanApplicationRepository();
        service = new LoanApplicationService(repository);
    }

    @Test
    void testApprovedApplication() {
        LoanApplicationRequest request = createValidRequest();

        LoanApplicationResponse response = service.process(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getApplicationId());
        assertNotNull(response.getOffer());
        assertNotNull(response.getRiskBand());
        assertNull(response.getRejectionReasons());
    }

    @Test
    void testRejectedDueToLowCreditScore() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setCreditScore(550);

        LoanApplicationResponse response = service.process(request);

        assertEquals("REJECTED", response.getStatus());
        assertNull(response.getOffer());
        assertNull(response.getRiskBand());
        assertNotNull(response.getRejectionReasons());
        assertTrue(response.getRejectionReasons().contains("CREDIT_SCORE_BELOW_MINIMUM"));
    }

    @Test
    void testRejectedDueToAgeTenureLimit() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setAge(60);
        request.getLoan().setTenureMonths(120); // 10 years, age + tenure = 70 > 65

        LoanApplicationResponse response = service.process(request);

        assertEquals("REJECTED", response.getStatus());
        assertNull(response.getOffer());
        assertNull(response.getRiskBand());
        assertNotNull(response.getRejectionReasons());
        assertTrue(response.getRejectionReasons().contains("AGE_TENURE_LIMIT_EXCEEDED"));
    }

    @Test
    void testRejectedDueToHighEmiToIncomeRatio() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setMonthlyIncome(20000.0); // Low income for high loan

        LoanApplicationResponse response = service.process(request);

        assertEquals("REJECTED", response.getStatus());
        assertNull(response.getOffer());
        assertNull(response.getRiskBand());
        // This could be rejected by either the specification or the 50% check in service
        assertNotNull(response.getRejectionReasons());
    }

    @Test
    void testRejectedDueToMultipleReasons() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setCreditScore(550); // Low credit score
        request.getApplicant().setAge(60);
        request.getLoan().setTenureMonths(120); // Age + tenure exceeds limit
        request.getApplicant().setMonthlyIncome(20000.0); // Low income

        LoanApplicationResponse response = service.process(request);

        assertEquals("REJECTED", response.getStatus());
        assertNull(response.getOffer());
        assertNull(response.getRiskBand());
        assertNotNull(response.getRejectionReasons());
        assertTrue(response.getRejectionReasons().size() >= 2);
    }

    @Test
    void testEdgeCaseCreditScoreAtMinimum() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setCreditScore(600); // Exactly at minimum

        LoanApplicationResponse response = service.process(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getOffer());
    }

    @Test
    void testEdgeCaseAgeTenureAtLimit() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setAge(30);
        request.getLoan().setTenureMonths(420); // 35 years, age + tenure = 65

        LoanApplicationResponse response = service.process(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getOffer());
    }

    @Test
    void testSelfEmployedApplicant() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setEmploymentType(EmploymentType.SELF_EMPLOYED);

        LoanApplicationResponse response = service.process(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getOffer());
    }

    @Test
    void testHighRiskBandApplicant() {
        LoanApplicationRequest request = createValidRequest();
        request.getApplicant().setCreditScore(620); // High risk band

        LoanApplicationResponse response = service.process(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getOffer());
        assertEquals(com.example.digitalLendingApp.domain.enums.RiskBand.HIGH, response.getRiskBand());
    }

    private LoanApplicationRequest createValidRequest() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        
        ApplicantRequest applicant = new ApplicantRequest();
        applicant.setAge(30);
        applicant.setMonthlyIncome(50000.0);
        applicant.setEmploymentType(EmploymentType.SALARIED);
        applicant.setCreditScore(750);
        request.setApplicant(applicant);
        
        LoanRequest loan = new LoanRequest();
        loan.setAmount(500000.0);
        loan.setTenureMonths(36);
        request.setLoan(loan);
        
        return request;
    }
}
