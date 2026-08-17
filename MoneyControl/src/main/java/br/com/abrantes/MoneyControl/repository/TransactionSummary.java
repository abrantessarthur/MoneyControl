package br.com.abrantes.MoneyControl.repository;

import java.math.BigDecimal;

public interface TransactionSummary {
    BigDecimal getTotalIncome();
    BigDecimal getTotalExpense();
}
