package br.com.zenon.fraud;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
    private final List<Transaction> fullList;
    public TransactionListRepository(List<Transaction> fullList) {
        this.fullList = fullList;
    }

    @Override
    public Optional<Transaction> findByOriginName(final String customerName){
//        for (Transaction transaction : fullList) {
//            if(transaction.customerOrig().name().equals(customerName)){
//                return Optional.of(transaction);
//            }
//        }
        return fullList.stream()
                .filter(t -> t.customerOrig().name().equals(customerName))
                .findFirst();
    }

    @Override
    public void save(Transaction transaction) {
    }
}
