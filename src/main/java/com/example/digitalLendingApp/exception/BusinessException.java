package com.example.digitalLendingApp.exception;

import java.util.List;

public class BusinessException extends RuntimeException {

    private final List<String> reasons;

    public BusinessException(List<String> reasons) {
        this.reasons = reasons;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
