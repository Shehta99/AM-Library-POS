package com.library.AmLibrary.Controllers;

import com.library.AmLibrary.Services.AnalyticsService;
import com.library.AmLibrary.Services.CustomerService;
import com.library.AmLibrary.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AppFeaturesController {

    private final CustomerService customerService;
    private final AnalyticsService analyticsService;

    // --- 1. العملاء والديون ---
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @PostMapping("/customers/{id}/pay")
    public ResponseEntity<Customer> payDebt(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        String notes = payload.getOrDefault("notes", "سداد نقدي").toString();
        return ResponseEntity.ok(customerService.recordPayment(id, amount, notes));
    }

    // --- 2. الرسوم البيانية والتحليلات ---
    @GetMapping("/analytics/top-sellers")
    public ResponseEntity<?> getTopSellers() {
        return ResponseEntity.ok(analyticsService.getTopSellingItems(6));
    }

    @GetMapping("/analytics/hourly")
    public ResponseEntity<?> getHourlySales() {
        return ResponseEntity.ok(analyticsService.getTodayHourlySales());
    }
}