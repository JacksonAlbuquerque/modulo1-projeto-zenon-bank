package br.com.zenon.fraud;

import java.util.List;

public class Main {

    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";

    static void main() throws Exception {

        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.readTransactions(PATH_FILE);
        int count = Math.min(transactions.size(), 10);
        for (int i = 0; i < count; i++) {
            System.out.println(transactions.get(i));
        }
    }
}
