package com.example.cattery.model;

public enum CatStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    PAID("Paid"),
    SOLD("Sold");

    private final String displayValue;

    private CatStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
