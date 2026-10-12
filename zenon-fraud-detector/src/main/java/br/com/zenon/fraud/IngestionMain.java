package br.com.zenon.fraud;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class IngestionMain {
    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";

    static void main() throws IOException {
        EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor();
        try (final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/zenon?rewriteBatchedStatements=true", "root", "senha123")) {
            TransactionSQLRepository repository = new TransactionSQLRepository(conn);
            Consumer<Transaction> transactionConsumer = repository::save;
//            Consumer<Transaction> transactionConsumer = transaction -> repository.save(transaction);
//            Consumer<Transaction> transactionConsumer = new Consumer<>() {
//                @Override
//                public void accept(Transaction transaction) {
//                    repository.save(transaction);
//                }
//            };
            for (int i = 0; i < 5; i++) {
                repository.cleanBD();
                long init = System.nanoTime();
                efficientTransactionIngestor.readAsStreamWithBufferedReader(PATH_FILE, transactionConsumer);
//                efficientTransactionIngestor.readAsStreamWithFiles(PATH_FILE,transactionConsumer);
                long end = System.nanoTime();
                System.out.println("Inserção no banco: readAsStreamWithBufferedReader");
//                System.out.println("Inserção no banco: readAsStreamWithFiles");
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
