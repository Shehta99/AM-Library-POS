package com.library.AmLibrary.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // اسم الكتاب أو الأداة المدرسية

    @Column(nullable = false)
    private String category; // تصنيف المنتج: "BOOK" أو "STATIONERY" (أدوات مدرسية)

    private String description; // وصف المنتج (المؤلف، دار النشر، أو تفاصيل الأداة)

    @Column(nullable = false)
    private BigDecimal price; // سعر البيع

    @Column(nullable = false)
    private Integer stockQuantity; // كمية المخزون المتاحة للبيع

    private String barcode; // باركود المنتج لتسهيل البحث والبيع السريع

    @Column(nullable = false)
    private BigDecimal costPrice;
}

