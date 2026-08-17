package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.enums.TypeTransactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionsProjection {
    Long getId();
    String getDescription();
    BigDecimal getAmount();
    TypeTransactional getTypeTransactional();
    Long getCategoryId();
    String getCategoryName();
    LocalDateTime getDate();
}
