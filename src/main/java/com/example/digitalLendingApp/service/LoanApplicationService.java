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
import com.example.digitalLendingApp.exception.BusinessException;
import com.example.digitalLendingApp.repository.InMemoryLoanApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LoanApplicationService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    private final InMemoryLoanApplicationRepository repository;
    private final StandardEmiCalculator emiCalculator;

    public LoanApplicationService(InMemoryLoanApplicationRepository repository,
                                  StandardEmiCalculator emiCalculator) {
        this.repository = repository;
        this.emiCalculator = emiCalculator;
    }

    public LoanApplicationResponse process(LoanApplicationRequest request) {

        log.info("Starting digital loan application process");
        UUID applicationId = UUID.randomUUID();

        // Risk Classification through strategy design pattern
        log.info("Generating risk classification bank through credit score");
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

        // Calculate Final Interest using runtime instance through factory design pattern
        log.info("Getting total interest premium as per the inputs in api request");
        InterestPremiumStrategyFactory factory =
                new InterestPremiumStrategyFactory();

        BigDecimal finalRate = BASE_RATE;

        for (InterestPremiumStrategy strategy : factory.getStrategies()) {
            finalRate = finalRate.add(strategy.premium(context));
        }

        // Calculate EMI through template design pattern
        log.info("Calculating Emi with given inputs");
        BigDecimal emi = emiCalculator.calculate(
                context.getAmount(),
                finalRate,
                context.getTenureMonths()
        );

        context.setEmi(emi);

        // Eligibility Check through specification design pattern
        log.info("Checking Eligibility");
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

        if (!failures.isEmpty()) {
            log.info("Generating rejected offer response with mentioned failure reason");
            response.setStatus("REJECTED");
            response.setRiskBand(null);
            response.setRejectionReasons(failures);

            repository.save(applicationId, response);
            return response;
        }

        if (emi.compareTo(context.getMonthlyIncome()
                .multiply(BigDecimal.valueOf(0.6))) > 0) {
            log.info("Offer is rejected due to emi exceeding 60% of monthly income");
            response.setStatus("REJECTED");
            response.setRiskBand(null);
            response.setRejectionReasons(List.of("EMI_EXCEEDS_60_PERCENT"));

            repository.save(applicationId, response);
            return response;
        }

        log.info("Generating accepted offer response");
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

        repository.save(applicationId, response);

        return response;
    }
}
