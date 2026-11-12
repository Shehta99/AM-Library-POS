package com.library.AmLibrary.Controllers;
import com.library.AmLibrary.Repositories.OrderRepository;
import com.library.AmLibrary.Services.OrderItemRequest;
import com.library.AmLibrary.Services.OrderService;
import com.library.AmLibrary.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // تفعيل CORS لتطبيق React المحلي
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    // تسجيل عملية بيع جديدة وإصدار الفاتورة
    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody List<OrderItemRequest> itemRequests) {
        try {
            Order completedOrder = orderService.createOrder(itemRequests);
            return ResponseEntity.ok(completedOrder);
        } catch (RuntimeException e) {
            // إرجاع رسالة خطأ واضحة في حال عدم توفر كمية كافية بالمخزن
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        // جلب جميع الفواتير مرتبة من الأحدث للأقدم
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        return ResponseEntity.ok(orders);
    }

    // داخل OrderController.java
    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnOrder(@PathVariable Long id) {
        try {
            Order returnedOrder = orderService.returnOrder(id);
            return ResponseEntity.ok(returnedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
