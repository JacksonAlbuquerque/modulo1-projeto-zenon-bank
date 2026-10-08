package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {
    private final String path;
    private long totalOfLines = 0;
    private long totalOfFrauds = 0;
    private BigDecimal totalValue = BigDecimal.ZERO;

    public TransactionReport(String path) {
        this.path = path;
    }

    public void processAndPrintResults() throws IOException {
        totalValue = BigDecimal.ZERO;
        totalOfFrauds = 0;
        totalOfLines = 0;
        try (Stream<String> readFile = Files.lines(Path.of(path))) {
            readFile.skip(1).forEach(s -> {
                Optional<Transaction> transaction = TransactionIngestor.parseLine(s);
                boolean present = transaction.isPresent();
                if (present) {
                    totalOfLines++;
                    if (transaction.get().isFraud()) {
                        totalOfFrauds++;
                    }
                    totalValue = totalValue.add(transaction.get().amount());
                }
            });
        }
        System.out.println("Total de linhas: " + totalOfLines);
        System.out.println("Total de fraudes: " + totalOfFrauds);
        System.out.println("Valor total transacionado: " + totalValue);
    }
}
