package br.com.zenon.fraud;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    private static final String PATH_FILE = "../data/PS_20174392719_1491204439457_log.csv";

    static void main(String[] args) throws IOException {
        Locale locale;
        if (args.length == 0) {
            locale = Locale.US;
        } else if (args[0].toLowerCase(Locale.ROOT).equals("br")) {
            locale = Locale.of("pt", "BR");   // Locale.of existe desde o Java 19
        } else if (args[0].toLowerCase(Locale.ROOT).equals("en")) {
            locale = Locale.US;
        } else {
            locale = Locale.US;
        }

        ResourceBundle mensagens = ResourceBundle.getBundle("report", locale);

        TransactionReport transactionReport = new TransactionReport(PATH_FILE);
        ReportSummary result = transactionReport.process();

        System.out.println(mensagens.getString("transaction.reportmain.totallines") + ": " + NumberFormat.getIntegerInstance(locale).format(result.totalOfLines()));
        System.out.println(mensagens.getString("transaction.reportmain.totalfrauds") + ": " + NumberFormat.getIntegerInstance(locale).format(result.totalOfFrauds()));
        System.out.println(mensagens.getString("transaction.reportmain.totalvalue") + ": " + NumberFormat.getCurrencyInstance(locale).format(result.totalValue()));


    }
}
