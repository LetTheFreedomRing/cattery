package com.example.cattery.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChargeRequestDTO {

    public enum Currency {
        EUR, USD;
    }
    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
