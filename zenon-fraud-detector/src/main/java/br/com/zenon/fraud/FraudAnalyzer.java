package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {
    public List<Transaction> getListTransactionFraud(List<Transaction> fullList) {
        return fullList.stream().filter(Transaction::isFraud).toList();
    }

    public List<Transaction> returnThreeBiggestFrauds(List<Transaction> listFrauds) {
        Comparator<Transaction> comparator = Comparator.comparing(Transaction::amount).reversed();
        return listFrauds.stream().sorted(comparator).limit(3).toList();
    }

    public List<String> returnFiveNamesBiggestFrauds(List<Transaction> listFrauds) {
        Comparator<Transaction> comparator = Comparator.comparing(Transaction::amount).reversed();
        return listFrauds.stream()
                .sorted(comparator)
                .map(t -> t.customerOrig().name())
                .distinct()
                .limit(5)
                .toList();
    }

    public BigDecimal returnsTotalValueFraud(List<Transaction> listFrauds) {
        return listFrauds.stream().map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<TransactionType, Long> returnNumberFraudByType(List<Transaction> listFrauds) {
        return listFrauds.stream()
                .collect(Collectors.groupingBy(Transaction::type
                        , () -> new EnumMap<>(TransactionType.class)
                        , Collectors.counting()));
    }
}
