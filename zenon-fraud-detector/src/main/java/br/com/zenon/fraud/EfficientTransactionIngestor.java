package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static br.com.zenon.fraud.TransactionIngestor.parseLine;

public class EfficientTransactionIngestor {
    private static final int MAX_TRANSACTIONS = 10000;
    private static final int BATCH_SIZE = 10000;

    public void readAsStreamWithFiles(String path, Consumer<Transaction> transactionConsumer) throws IOException {
        try (Stream<String> file = Files.lines(Path.of(path)).skip(1)) {
            file.map(TransactionIngestor::parseLine).flatMap(Optional::stream).limit(MAX_TRANSACTIONS).forEach(transactionConsumer);
        }
    }

    public void readAsStreamWithBufferedReader(String path, Consumer<Transaction> transactionConsumer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine();
            long count = 0;
//            AtomicLong count = new AtomicLong();          // Com AtomicLong, começa em 0
            while ((line = reader.readLine()) != null) {
                Optional<Transaction> transaction = parseLine(line);
                if (transaction.isPresent()){
                    transactionConsumer.accept(transaction.get());
                    count++;
//                    count.incrementAndGet(); // Com AtomicLong
                }
                if (count >= MAX_TRANSACTIONS) {
//                if (count.get() >= MAX_TRANSACTIONS) { // Com AtomicLong
                    break;
                }
            }
        }
    }

    public void readBatch(String path, Consumer<List<Transaction>> batchConsumer) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine();
            List<Transaction> lote = new ArrayList<>(BATCH_SIZE);
            while ((line = reader.readLine()) != null) {
                Optional<Transaction> transaction = parseLine(line);
                if (transaction.isPresent()){
                    lote.add(transaction.get());
                }
                if (lote.size() >= BATCH_SIZE) { // Com AtomicLong
                    batchConsumer.accept(lote);
                    lote = new ArrayList<>(BATCH_SIZE);
                }
            }
            if (!lote.isEmpty()) { // Com AtomicLong
                batchConsumer.accept(lote);
            }
        }
    }
}
