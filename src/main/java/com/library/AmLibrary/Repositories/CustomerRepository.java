package com.library.AmLibrary.Repositories;


import com.library.AmLibrary.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // دالة مساعدة اختيارية للبحث برقم الهاتف
    Optional<Customer> findByPhone(String phone);

    // دالة مساعدة اختيارية للبحث بالاسم
    List<Customer> findByNameContainingIgnoreCase(String name);
}
