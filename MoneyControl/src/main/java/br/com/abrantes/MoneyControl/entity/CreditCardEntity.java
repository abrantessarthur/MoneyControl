package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
