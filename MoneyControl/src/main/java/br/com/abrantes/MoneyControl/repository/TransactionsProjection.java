package br.com.abrantes.MoneyControl.repository;

import br.com.abrantes.MoneyControl.entity.CategoryEntity;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionsProjection {
    Long getId();
    String getDescription();
    BigDecimal getAmount();
    TypeTransactional getTypeTransaction();
    CategoryEntity getCategory();
    LocalDateTime getDate();
}
