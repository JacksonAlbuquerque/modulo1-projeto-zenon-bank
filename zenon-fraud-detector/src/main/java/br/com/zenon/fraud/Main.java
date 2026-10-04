package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";
    private static final String PATH_FILE_TEST = "../data/paysim_with_bad_data.csv";

    static void main() throws IOException {

        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.readTransactions(PATH_FILE_TEST);
        System.out.println(transactions.size());
        for (Transaction t : transactions){
            System.out.println(t);
        }
    }
}
