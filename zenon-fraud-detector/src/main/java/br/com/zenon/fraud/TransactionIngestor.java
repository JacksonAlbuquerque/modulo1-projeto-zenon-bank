package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final int MAX_TRANSACTIONS = 100000;

    public List<Transaction> readTransactions(String path) throws IOException {

        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
//                Optional<Transaction> t = parseLine(line);
//                if (t.isPresent()) {
//                    transactions.add(t.get());
//                }
                parseLine(line).ifPresent(transactions::add);
                if (transactions.size() >= MAX_TRANSACTIONS) {
                    break;
                }
            }
        }
        return transactions;
    }

    public static Optional<Transaction> parseLine(String line) {
        try {
            String[] fields = line.split(",", -1);
            if (fields.length != 11) throw new IllegalArgumentException("expected 11 fields but got " + fields.length);
            int index = 0;
            int step = Integer.parseInt(fields[index++]);
            TransactionType type = TransactionType.valueOf(fields[index++]);
            BigDecimal amount = new BigDecimal(fields[index++]);
            String nameOrig = fields[index++];
            BigDecimal oldBalanceOrig = new BigDecimal(fields[index++]);
            BigDecimal newBalanceOrig = new BigDecimal(fields[index++]);
            TransactionCustomer customerOrig = new TransactionCustomer(nameOrig, oldBalanceOrig, newBalanceOrig);
            String nameDest = fields[index++];
            BigDecimal oldBalanceDest = new BigDecimal(fields[index++]);
            BigDecimal newBalanceDest = new BigDecimal(fields[index++]);
            TransactionCustomer customerDest = new TransactionCustomer(nameDest, oldBalanceDest, newBalanceDest);
            boolean isFraud = "1".equals(fields[index++]);
            boolean isFlaggedFraud = "1".equals(fields[index]);

            return Optional.of(new Transaction(step, type, amount, customerOrig, customerDest, isFraud, isFlaggedFraud));
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + line + " | " + e);
            return Optional.empty();
        }
    }

}
