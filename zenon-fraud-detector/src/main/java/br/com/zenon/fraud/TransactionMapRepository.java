package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {
//    private Map<String,Transaction> map = new HashMap<>();
    private final Map<String,Transaction> map;

    public TransactionMapRepository(List<Transaction> listFull) {
//        for (Transaction transaction : listFull) {
//            map.put(transaction.customerOrig().name(),transaction);
//        }
        map = listFull.stream().collect(Collectors.toMap(t-> t.customerOrig().name(), t->t, (first, second) -> first));
    }

    @Override
    public Optional<Transaction> searchTransactionByCustomerName(final String customerName){
        return Optional.ofNullable(map.get(customerName));
    }
}
