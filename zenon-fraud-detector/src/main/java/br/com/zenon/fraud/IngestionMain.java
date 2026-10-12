package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class IngestionMain {
    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";
    private static final int numberOfThreads = 8;
    private static final boolean useVirtualThread = false;

    static void main() throws IOException {
        EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor(numberOfThreads, useVirtualThread);
        String URL = "jdbc:mysql://localhost:3306/zenon?rewriteBatchedStatements=true";
        String user = "root";
        String pass = "senha123";
        TransactionSQLRepository repository = new TransactionSQLRepository(URL,user,pass);
        Consumer<List<Transaction>> consumers = new Consumer<List<Transaction>>() {
            @Override
            public void accept(List<Transaction> transactions) {
                repository.saveAll(transactions);
            }
        };
        System.out.println("Inserção no banco: readBatch com " + numberOfThreads + " Threads Vituais.");
        for (int i = 0; i < 3; i++) {
            repository.cleanBD();
            long init = System.nanoTime();
            efficientTransactionIngestor.readBatch(PATH_FILE, consumers);
            long end = System.nanoTime();
            System.out.println("Inserção no banco: readBatch");
            System.out.println("tempo: " + (end - init) + "ns");
            System.out.println("tempo: " + ((end - init) / 1000) + "us");
            System.out.println("tempo: " + ((end - init) / 1000000) + "ms");
            repository.checkBD();
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }
}
