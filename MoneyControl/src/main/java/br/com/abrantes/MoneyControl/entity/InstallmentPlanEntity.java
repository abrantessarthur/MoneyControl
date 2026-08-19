package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "installment_plans")
@Builder
@AllArgsConstructor
@NoArgsConstructor
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