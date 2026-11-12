package com.library.AmLibrary.Repositories;

import com.library.AmLibrary.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // دالة الفلترة الديناميكية حسب اسم الصنف المختار من الـ Front-end
    List<Product> findByCategory(String category);

    // داخل ProductRepository.java
    List<Product> findByStockQuantityLessThanEqual(int threshold);

}

