package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionCustomer(String name, BigDecimal oldBalance, BigDecimal newBalance) {
    public TransactionCustomer {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("name should not be empty");
        }
        if(oldBalance == null){
            throw new IllegalArgumentException("oldBalance should not be null");
        }
        if(newBalance == null){
            throw new IllegalArgumentException("newBalance should not be null");
        }

        if(oldBalance.signum() == -1){
            throw new IllegalArgumentException("oldBalance should be positive: " + oldBalance);
        }
        if(newBalance.signum() == -1){
            throw new IllegalArgumentException("newBalance should be positive: " + newBalance);
        }
    }
}
