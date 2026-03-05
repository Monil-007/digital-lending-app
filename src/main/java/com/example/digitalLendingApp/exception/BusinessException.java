package com.example.digitalLendingApp.exception;

import java.util.List;

public class BusinessException extends RuntimeException {

    private final List<String> reasons;

    public BusinessException(String message, List<String> reasons) {
        super(message);
        this.reasons = reasons;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
