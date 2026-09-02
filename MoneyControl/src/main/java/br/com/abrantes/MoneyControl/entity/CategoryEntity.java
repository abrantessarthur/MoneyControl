package br.com.abrantes.MoneyControl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorys")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
