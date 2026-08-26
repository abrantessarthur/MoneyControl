package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastFourDigits;
    private BigDecimal creditLimit;
    private Integer closingDay;
    private Integer dueDay;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
