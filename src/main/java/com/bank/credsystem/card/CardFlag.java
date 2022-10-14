package com.bank.credsystem.card;

public enum CardFlag {
    MASTERCARD, VISA, AMERICAN_EXPRESS;

    public String getFlagNumber() {
        switch (this) {
            case MASTERCARD:
                return "5109";
            case VISA:
                return "4001";
            case AMERICAN_EXPRESS:
                return "3403";
            default:
                return null;
        }
    }
}
