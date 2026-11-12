package com.library.AmLibrary.Repositories;
import com.library.AmLibrary.entity.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {
    // جلب سجل مدفوعات عميل معين
    List<CustomerPayment> findByCustomerIdOrderByPaymentDateDesc(Long customerId);
}

