package com.example.digitalLendingApp.Controller;


import com.example.digitalLendingApp.Controller.dto.Requests.LoanApplicationRequest;
import com.example.digitalLendingApp.Controller.dto.Responses.LoanApplicationResponse;
import com.example.digitalLendingApp.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> apply(@Valid @RequestBody LoanApplicationRequest request) {

        return ResponseEntity.ok(service.process(request));
    }
}

