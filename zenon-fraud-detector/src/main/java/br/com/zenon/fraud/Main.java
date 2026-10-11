package br.com.zenon.fraud;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";
    private static final String PATH_FILE_TEST = "../data/paysim_with_bad_data.csv";

    static void main() throws IOException {

        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.readTransactions(PATH_FILE);
//        System.out.println(transactions.size());
//        for (Transaction t : transactions){
//            System.out.println(t);
//        }
        System.out.println(transactions.size());
        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer();
        List<Transaction> frauds = fraudAnalyzer.getListTransactionFraud(transactions);
        System.out.println("1. Total de Fraudes: " + frauds.size());
        List<Transaction> threeBiggestFrauds = fraudAnalyzer.returnThreeBiggestFrauds(frauds);
        System.out.println("2. Top 3 Fraudes de Maior Valor:");
//        for (Transaction threeBiggestFraud : threeBiggestFrauds) {
//            System.out.println(threeBiggestFraud.amount().setScale(2).toPlainString());
//        }
        threeBiggestFrauds.forEach(t -> System.out.println(t.amount().setScale(2, RoundingMode.HALF_EVEN).toPlainString()));
        System.out.println("3. Clientes Suspeitos:");
        List<String> namesBiggestFrauds = fraudAnalyzer.returnFiveNamesBiggestFrauds(frauds);
        namesBiggestFrauds.forEach(System.out::println);
        System.out.println("4. Prejuízo Total: " + fraudAnalyzer.returnsTotalValueFraud(frauds));
        System.out.println("5. Fraudes por Tipo:");
        fraudAnalyzer.returnNumberFraudByType(frauds)
                .forEach((type, count) ->
                        System.out.println(" - " + type + ": " + count));

        TransactionRepository listRepository = new TransactionListRepository(transactions);
        //String name = "C1231006815";
        //String name = "C12345";
        String name = "C1868032458";
        long init = System.nanoTime();
//        mapRepository.searchTransactionByCustomerName(name).ifPresentOrElse(
//                t -> System.out.println(t),
//                () -> System.out.println("Transação não encontrada para o cliente "+name));
        Optional<Transaction> result = listRepository.findByOriginName(name);
        long end = System.nanoTime();
        result.ifPresentOrElse(
                t -> System.out.println(t),
                () -> System.out.println("Transação não encontrada para o cliente "+name));
        System.out.println("tempo: " + (end - init) + "ns");
        System.out.println("tempo: " + ((end - init)/1000)+ "us");
        System.out.println("tempo: " + ( (end - init) / 1000000) + "ms");

        TransactionRepository mapRepository = new TransactionMapRepository(transactions);
        //String name = "C1231006815";
        //String name = "C12345";
//        String name2 = "C1868032458";
        long init2 = System.nanoTime();
//        mapRepository.findByOriginName(name).ifPresentOrElse(
//                t -> System.out.println(t),
//                () -> System.out.println("Transação não encontrada para o cliente "+name));
        Optional<Transaction> result2 = mapRepository.findByOriginName(name);
        long end2 = System.nanoTime();
        result2.ifPresentOrElse(
                t -> System.out.println(t),
                () -> System.out.println("Transação não encontrada para o cliente "+name));
        System.out.println("tempo: " + (end2 - init2) + "ns");
        System.out.println("tempo: " + ((end2 - init2)/1000)+ "us");
        System.out.println("tempo: " + ( (end2 - init2) / 1000000) + "ms");
    }
}
