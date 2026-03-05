package com.example.digitalLendingApp.repository;

import com.example.digitalLendingApp.Controller.dto.Responses.LoanApplicationResponse;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLoanApplicationRepository {

    private final Map<UUID, LoanApplicationResponse> store = new ConcurrentHashMap<>();

    public void save(UUID id, LoanApplicationResponse response) {
        store.put(id, response);
    }

    public LoanApplicationResponse findById(UUID id) {
        return store.get(id);
    }
}
