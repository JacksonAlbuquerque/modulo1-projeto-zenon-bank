package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static br.com.zenon.fraud.TransactionIngestor.parseLine;

public class EfficientTransactionIngestor {
    private static final int MAX_TRANSACTIONS = 10000;
    //    private static final int BATCH_SIZE = 10000;
    //    private static final int BATCH_SIZE = 5000;
    private static final int BATCH_SIZE = 2500;
    //    private static final int BATCH_SIZE = 1250;
    private final int numberOfThreads;
    boolean useVirtualThread;

    public EfficientTransactionIngestor(int numberOfThreads, boolean useVirtualThread) {
        this.numberOfThreads = numberOfThreads;
        this.useVirtualThread = useVirtualThread;
    }

    public void readAsStream(String path, Consumer<Transaction> transactionConsumer) throws IOException {
        readAsStreamWithBufferedReader(path, transactionConsumer);
    }

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
                if (transaction.isPresent()) {
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

    public void readBatch(String path, Consumer<List<Transaction>> batchConsumer) throws IOException {
        ExecutorService executor;
        AtomicLong countError = new AtomicLong();
        if (useVirtualThread) {
            executor = Executors.newVirtualThreadPerTaskExecutor();      // virtual
        } else {
            executor = Executors.newFixedThreadPool(numberOfThreads);
        }
        Semaphore semaphore = new Semaphore(numberOfThreads);
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine();
            List<Transaction> lote = new ArrayList<>(BATCH_SIZE);
            while ((line = reader.readLine()) != null) {
                Optional<Transaction> transaction = parseLine(line);
                if (transaction.isPresent()) {
                    lote.add(transaction.get());
                }
                if (lote.size() >= BATCH_SIZE) {
                    semaphore.acquire();
                    List<Transaction> loteCheio = lote;
                    executor.submit(() -> {
                        try {
                            batchConsumer.accept(loteCheio);
                        } catch (Throwable e) {
                            countError.incrementAndGet();
                            e.printStackTrace();
                        } finally {
                            semaphore.release();
                        }
                    });
                    lote = new ArrayList<>(BATCH_SIZE);
                }
            }
            if (!lote.isEmpty()) {
                semaphore.acquire();
                List<Transaction> loteCheio = lote;
                executor.submit(() -> {
                    try {
                        batchConsumer.accept(loteCheio);
                    } catch (Throwable e) {
                        countError.incrementAndGet();
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                });
            }
            executor.shutdown();
            boolean b = executor.awaitTermination(60, TimeUnit.MINUTES);
            System.out.println("lotes com erros: " + countError.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
