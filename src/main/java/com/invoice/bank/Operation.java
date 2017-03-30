package com.invoice.bank;

public class Operation {
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "amount='" + amount + '\'' +
                '}';
    }
}