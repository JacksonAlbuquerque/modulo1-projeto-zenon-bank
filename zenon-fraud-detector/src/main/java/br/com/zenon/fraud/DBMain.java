package br.com.zenon.fraud;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class DBMain {
    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";

    static void main() throws IOException, SQLException {
        try (final Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/zenon", "root", "senha123")) {

            TransactionIngestor transactionIngestor = new TransactionIngestor();
            List<Transaction> transactions = transactionIngestor.readTransactions(PATH_FILE);
            TransactionSQLRepository transactionSQLRepository = new TransactionSQLRepository(conn);
            {// Se for executar esse comanod mais de uma vez lembrar de sempre limpar o banco antes da proxima execução TRUNCATE TABLE TRANSACTIONS;
                long init1 = System.nanoTime();
                for (Transaction transaction : transactions) {
                    transactionSQLRepository.save(transaction);
                }
                long end1 = System.nanoTime();
                System.out.println("tempo: " + (end1 - init1) + "ns");
                System.out.println("tempo: " + ((end1 - init1) / 1000) + "us");
                System.out.println("tempo: " + ((end1 - init1) / 1000000) + "ms");
            }

            for (int i = 0; i < 3; i++) {
                long init = System.nanoTime();
                Optional<Transaction> c1231006815 = transactionSQLRepository.findByOriginName("C1231006815");
                long end = System.nanoTime();
                System.out.println("tempo: " + (end - init) + "ns");
                System.out.println("tempo: " + ((end - init) / 1000) + "us");
                System.out.println("tempo: " + ((end - init) / 1000000) + "ms");
                System.out.println(c1231006815);

                init = System.nanoTime();
                Optional<Transaction> C12345 = transactionSQLRepository.findByOriginName("C12345");
                end = System.nanoTime();
                System.out.println("tempo: " + (end - init) + "ns");
                System.out.println("tempo: " + ((end - init) / 1000) + "us");
                System.out.println("tempo: " + ((end - init) / 1000000) + "ms");
                System.out.println(C12345);
            }
        }
    }
}
