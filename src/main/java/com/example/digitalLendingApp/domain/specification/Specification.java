package com.example.digitalLendingApp.domain.specification;

public interface Specification<T> {
    boolean isSatisfiedBy(T context);
    String reason();
}
