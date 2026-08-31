package br.com.abrantes.MoneyControl.entity;

import br.com.abrantes.MoneyControl.enums.RecurrenceFrequency;
import br.com.abrantes.MoneyControl.enums.TypeTransactional;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recurring_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TypeTransactional typeTransactional;

    @Enumerated(EnumType.STRING)
    private RecurrenceFrequency frequency;

    private LocalDate startDate;

    private LocalDate nextExecutionDate;

    private LocalDate endDate;

    private LocalDate lastExecutionDate;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
