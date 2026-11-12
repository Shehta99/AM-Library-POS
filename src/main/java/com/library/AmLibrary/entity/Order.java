package com.library.AmLibrary.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime orderDate; // تاريخ ووقت عملية البيع

    @Column(nullable = false)
    private BigDecimal totalAmount; // المبلغ الإجمالي للفاتورة

    // داخل كلاس Order.java
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference // 👈 تُخبر جاكسون بفتح وتسجيل هذه القائمة بشكل طبيعي
    private List<OrderItem> items;

    @Column(nullable = false)
    private String status = "COMPLETED";

    @Column(nullable = false)
    private String paymentType = "CASH"; // CASH أو CREDIT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
