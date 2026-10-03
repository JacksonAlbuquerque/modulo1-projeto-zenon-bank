package br.com.zenon.fraud;

import java.math.BigDecimal;

public class Main {

    static void main() {
        int counterTransaction = 0;
        TransactionCustomer customerOrg1 = new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36"));
        TransactionCustomer customerDest1 = new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0"));
        Transaction transaction1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"), customerOrg1, customerDest1, false, false);

//        System.out.println(transaction1.toString());
        System.out.println(formatTransaction(++counterTransaction,transaction1));

        TransactionCustomer customerOrg2 = new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0"));
        TransactionCustomer customerDest2 = new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63"));
        Transaction transaction2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"), customerOrg2, customerDest2, true, false);

//        System.out.println(transaction2.toString());
        System.out.print(formatTransaction(++counterTransaction,transaction2));
    }

    private static String formatTransaction(int count, Transaction t) {
        return String.format("Transação %d%n" +
                        "\tstep:%d%n" +
                        "\ttype:%s%n" +
                        "\tamount:%s%n" +
                        "\tnameOrig:%s%n" +
                        "\toldbalanceOrg:%s%n" +
                        "\tnewbalanceOrig:%s%n" +
                        "\tnameDest:%s%n" +
                        "\toldbalanceDest:%s%n" +
                        "\tnewbalanceDest:%s%n" +
                        "\tisFraud:%s%n" +
                        "\tisFlaggedFraud:%s%n"
                , count,
                t.step(),
                t.type(),
                t.amount(),
                t.customerOrig().name(),
                t.customerOrig().oldBalance(),
                t.customerOrig().newBalance(),
                t.customerDest().name(),
                t.customerDest().oldBalance(),
                t.customerDest().newBalance(),
                (t.isFraud() ? "1" : "0"),
                (t.isFlaggedFraud() ? "1" : "0")
        );
    }
}
