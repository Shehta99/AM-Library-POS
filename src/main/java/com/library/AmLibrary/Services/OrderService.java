package com.library.AmLibrary.Services;
import com.library.AmLibrary.Repositories.OrderRepository;
import com.library.AmLibrary.Repositories.ProductRepository;
import com.library.AmLibrary.entity.Order;
import com.library.AmLibrary.entity.OrderItem;
import com.library.AmLibrary.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(List<OrderItemRequest> itemRequests) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest request : itemRequests) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("المنتج غير موجود بـ ID: " + request.getProductId()));

            // التأكد من توفر الكمية الكافية بالمخزن لعملية البيع
            if (product.getStockQuantity() < request.getQuantity()) {
                throw new RuntimeException("الكمية المطلوبة من المنتج '" + product.getName() + "' غير متوفرة. المتاح: " + product.getStockQuantity());
            }

            // تحديث الكمية المتاحة في المخزن (خصم المخزون)
            product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
            productRepository.save(product);

            // إعداد تفاصيل الفاتورة
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(request.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setCostPrice(product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO);

            orderItems.add(orderItem);

            // إضافة القيمة للمجموع الإجمالي
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }
    @Transactional
    public Order returnOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("الفاتورة غير موجودة"));

        if ("RETURNED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("تم استرجاع هذه الفاتورة بالفعل سابقاً!");
        }

        // إعادة الكميات إلى رصيد المخزن
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("RETURNED");
        return orderRepository.save(order);
    }

    // 2. تحديث دالة التقرير اليومي لاستبعاد الفواتير المرتجعة
    public DailyReportDTO getDailyReport(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        long validOrdersCount = 0;

        for (Order order : orders) {
            // استبعاد أي فاتورة تمت استعادتها
            if ("RETURNED".equalsIgnoreCase(order.getStatus())) {
                continue;
            }

            validOrdersCount++;
            totalRevenue = totalRevenue.add(order.getTotalAmount());
            for (OrderItem item : order.getItems()) {
                BigDecimal itemCost = item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalCost = totalCost.add(itemCost);
            }
        }

        BigDecimal netProfit = totalRevenue.subtract(totalCost);
        double margin = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? netProfit.divide(totalRevenue, 4, RoundingMode.HALF_UP).doubleValue() * 100
                : 0.0;

        return new DailyReportDTO(date, validOrdersCount, totalRevenue, totalCost, netProfit, margin);
    }

}
