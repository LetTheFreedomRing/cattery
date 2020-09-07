package com.example.cattery.model;

public enum CatClass {
    PET("Pet"),
    BREEDING("Breeding"),
    SHOW("Show"),
    EXCLUSIVE("Exclusive");

    private final String displayValue;

    private CatClass(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
