package br.com.zenon.fraud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IngestionMain {
    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";

    static void main() throws IOException {
        EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor();
        try (final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/zenon?rewriteBatchedStatements=true", "root", "senha123")) {
            TransactionSQLRepository repository = new TransactionSQLRepository(conn);
            Consumer<List<Transaction>> consumers = new Consumer<List<Transaction>>() {
                @Override
                public void accept(List<Transaction> transactions) {
                    repository.saveAll(transactions);
                }
            };
            for (int i = 0; i < 5; i++) {
                repository.cleanBD();
                long init = System.nanoTime();
                efficientTransactionIngestor.readBatch(PATH_FILE,consumers);
                long end = System.nanoTime();
                System.out.println("Inserção no banco: readBatch");
                System.out.println("tempo: " + (end - init) + "ns");
                System.out.println("tempo: " + ((end - init) / 1000) + "us");
                System.out.println("tempo: " + ((end - init) / 1000000) + "ms");
                System.out.println("------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
