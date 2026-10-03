package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(int step, TransactionType type, BigDecimal amount, TransactionCustomer customerOrig,
                          TransactionCustomer customerDest, boolean isFraud, boolean isFlaggedFraud) {

}
