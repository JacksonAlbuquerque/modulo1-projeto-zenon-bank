package br.com.zenon.fraud;

import java.math.BigDecimal;

public record ReportSummary(long totalOfLines, long totalOfFrauds, BigDecimal totalValue) {
}
