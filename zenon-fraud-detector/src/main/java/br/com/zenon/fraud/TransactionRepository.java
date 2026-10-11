package br.com.zenon.fraud;


import java.util.Optional;

public interface TransactionRepository {
    public Optional<Transaction> findByOriginName(final String customerName);
    public void save(Transaction transaction);
}
