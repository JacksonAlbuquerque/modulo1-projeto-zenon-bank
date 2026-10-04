package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(int step, TransactionType type, BigDecimal amount, TransactionCustomer customerOrig,
                          TransactionCustomer customerDest, boolean isFraud, boolean isFlaggedFraud) {
    public Transaction {
        if(type == null){
            throw new IllegalArgumentException("type should not be null");
        }
        if(amount == null){
            throw new IllegalArgumentException("amount should not be null");
        }
        if(customerOrig == null){
            throw new IllegalArgumentException("customerOrig should not be null");
        }
        if(customerDest == null){
            throw new IllegalArgumentException("customerDest should not be null");
        }

        if(step < 1){
            throw new IllegalArgumentException("step should be positive: " + step);
        }
        if(amount.signum() == -1){
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }
    }
}
