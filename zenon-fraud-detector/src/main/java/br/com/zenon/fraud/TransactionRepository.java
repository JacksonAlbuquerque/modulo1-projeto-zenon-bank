package br.com.zenon.fraud;


import java.util.Optional;

public interface TransactionRepository {
    public Optional<Transaction> searchTransactionByCustomerName(final String customerName);
}
