package com.example.digitalLendingApp;

import com.example.digitalLendingApp.Controller.dto.Responses.LoanApplicationResponse;
import com.example.digitalLendingApp.repository.InMemoryLoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryLoanApplicationRepositoryTest {

    private InMemoryLoanApplicationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLoanApplicationRepository();
    }

    @Test
    void testSaveAndFindById() {
        UUID id = UUID.randomUUID();
        LoanApplicationResponse response = createTestResponse();

        repository.save(id, response);
        LoanApplicationResponse found = repository.findById(id);

        assertNotNull(found);
        assertEquals(response.getApplicationId(), found.getApplicationId());
        assertEquals(response.getStatus(), found.getStatus());
    }

    @Test
    void testFindByIdReturnsNullForNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();
        LoanApplicationResponse found = repository.findById(nonExistentId);
        
        assertNull(found);
    }

    @Test
    void testSaveOverwritesExistingEntry() {
        UUID id = UUID.randomUUID();
        LoanApplicationResponse originalResponse = createTestResponse();
        originalResponse.setStatus("APPROVED");
        
        repository.save(id, originalResponse);
        LoanApplicationResponse found = repository.findById(id);
        assertEquals("APPROVED", found.getStatus());

        LoanApplicationResponse updatedResponse = createTestResponse();
        updatedResponse.setStatus("REJECTED");
        updatedResponse.setApplicationId(id);
        
        repository.save(id, updatedResponse);
        LoanApplicationResponse foundAfterUpdate = repository.findById(id);
        assertEquals("REJECTED", foundAfterUpdate.getStatus());
    }

    @Test
    void testSaveMultipleEntries() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        LoanApplicationResponse response1 = createTestResponse();
        response1.setApplicationId(id1);
        
        LoanApplicationResponse response2 = createTestResponse();
        response2.setApplicationId(id2);

        repository.save(id1, response1);
        repository.save(id2, response2);

        LoanApplicationResponse found1 = repository.findById(id1);
        LoanApplicationResponse found2 = repository.findById(id2);

        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals(id1, found1.getApplicationId());
        assertEquals(id2, found2.getApplicationId());
        assertNotSame(found1, found2);
    }

    private LoanApplicationResponse createTestResponse() {
        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId(UUID.randomUUID());
        response.setStatus("APPROVED");
        response.setRiskBand(com.example.digitalLendingApp.domain.enums.RiskBand.LOW);
        
        com.example.digitalLendingApp.Controller.dto.Responses.OfferResponse offer = 
            new com.example.digitalLendingApp.Controller.dto.Responses.OfferResponse();
        offer.setInterestRate(BigDecimal.valueOf(12.5));
        offer.setTenureMonths(36);
        offer.setEmi(BigDecimal.valueOf(16666.67));
        offer.setTotalPayable(BigDecimal.valueOf(600000.00));
        
        response.setOffer(offer);
        return response;
    }
}
