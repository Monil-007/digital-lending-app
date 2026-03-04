package com.example.digitalLendingApp.service;

import com.example.digitalLendingApp.Controller.dto.Requests.*;
import com.example.digitalLendingApp.Controller.dto.Responses.*;
import com.example.digitalLendingApp.domain.enums.RiskBand;
import com.example.digitalLendingApp.domain.factory.InterestPremiumStrategyFactory;
import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.specification.*;
import com.example.digitalLendingApp.domain.strategy.CreditScoreRiskStrategy;
import com.example.digitalLendingApp.domain.strategy.InterestPremiumStrategy;
import com.example.digitalLendingApp.domain.template.StandardEmiCalculator;
import com.example.digitalLendingApp.repository.InMemoryLoanApplicationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LoanApplicationService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    private final InMemoryLoanApplicationRepository repository;

    public LoanApplicationService(InMemoryLoanApplicationRepository repository) {
        this.repository = repository;
    }

    public LoanApplicationResponse process(LoanApplicationRequest request) {

        UUID applicationId = UUID.randomUUID();

        // Risk Classification
        CreditScoreRiskStrategy riskStrategy = new CreditScoreRiskStrategy();
        RiskBand riskBand = riskStrategy.classify(
                request.getApplicant().getCreditScore()
        );

        // Build Context
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(request.getLoan().getAmount()),
                request.getLoan().getTenureMonths(),
                request.getApplicant().getAge(),
                BigDecimal.valueOf(request.getApplicant().getMonthlyIncome()),
                request.getApplicant().getEmploymentType(),
                request.getApplicant().getCreditScore(),
                riskBand
        );

        // Calculate Final Interest
        InterestPremiumStrategyFactory factory =
                new InterestPremiumStrategyFactory();

        BigDecimal finalRate = BASE_RATE;

        for (InterestPremiumStrategy strategy : factory.getStrategies()) {
            finalRate = finalRate.add(strategy.premium(context));
        }

        // Calculate EMI
        StandardEmiCalculator emiCalculator = new StandardEmiCalculator();
        BigDecimal emi = emiCalculator.calculate(
                context.getAmount(),
                finalRate,
                context.getTenureMonths()
        );

        context.setEmi(emi);

        // Eligibility Check
        EligibilityEvaluator evaluator = new EligibilityEvaluator(
                List.of(
                        new CreditScoreSpecification(),
                        new AgeTenureSpecification(),
                        new EmiToIncomeSpecification()
                )
        );

        List<String> failures = evaluator.evaluate(context);

        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId(applicationId);

        if (!failures.isEmpty() ||
                emi.compareTo(context.getMonthlyIncome()
                        .multiply(BigDecimal.valueOf(0.5))) > 0) {

            response.setStatus("REJECTED");
            response.setRiskBand(null);
            response.setRejectionReasons(failures);
        } else {

            BigDecimal totalPayable =
                    emi.multiply(BigDecimal.valueOf(context.getTenureMonths()));

            OfferResponse offer = new OfferResponse();
            offer.setInterestRate(finalRate);
            offer.setTenureMonths(context.getTenureMonths());
            offer.setEmi(emi);
            offer.setTotalPayable(totalPayable);

            response.setStatus("APPROVED");
            response.setRiskBand(riskBand);
            response.setOffer(offer);
        }

        repository.save(applicationId, response);

        return response;
    }
}
