package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "installment_plans")
public class InstallmentPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal totalAmount;
    private Integer totalInstallments;
    private LocalDate firstDueDate;

    @ManyToOne
    private CategoryEntity category;

    @ManyToOne
    private UserEntity user;
}