package com.library.AmLibrary.Services;

import com.library.AmLibrary.Repositories.CustomerPaymentRepository;
import com.library.AmLibrary.Repositories.CustomerRepository;
import com.library.AmLibrary.entity.Customer;
import com.library.AmLibrary.entity.CustomerPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerPaymentRepository paymentRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        if (customer.getBalance() == null) {
            customer.setBalance(BigDecimal.ZERO);
        }
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer recordPayment(Long customerId, BigDecimal amount, String notes) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("العميل غير مسجل"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("يجب أن يكون مبلغ الدفعة أكبر من الصفر");
        }

        CustomerPayment payment = new CustomerPayment();
        payment.setCustomer(customer);
        payment.setAmount(amount);
        payment.setNotes(notes);
        paymentRepository.save(payment);

        // تخفيض رصيد المديونية
        customer.setBalance(customer.getBalance().subtract(amount));
        return customerRepository.save(customer);
    }
}