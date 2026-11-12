package com.library.AmLibrary.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // المبلغ المستحق عليه

    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();
}

