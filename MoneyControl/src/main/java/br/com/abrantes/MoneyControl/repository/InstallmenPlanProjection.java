package br.com.abrantes.MoneyControl.repository;

import java.math.BigDecimal;

public interface InstallmenPlanProjection {
    Long getId();
    String getDescription();
    BigDecimal getTotalAmount();
    Integer getTotalInstallments();
    Long getCategoryId();
}
