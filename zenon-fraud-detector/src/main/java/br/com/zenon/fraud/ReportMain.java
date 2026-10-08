package br.com.zenon.fraud;

import java.io.IOException;

public class ReportMain {

    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";
    static void main() throws IOException {
        TransactionReport transactionReport = new TransactionReport(PATH_FILE);
        transactionReport.processAndPrintResults();
    }
}
