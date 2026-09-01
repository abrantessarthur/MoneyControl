package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_goal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FinancialGoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amountToAchive;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private LocalDate initialDate;

    private LocalDate finalDate;

    private BigDecimal amountLeft;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}
