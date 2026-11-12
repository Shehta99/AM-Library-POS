package com.library.AmLibrary.Services;

import com.library.AmLibrary.Repositories.OrderRepository;
import com.library.AmLibrary.entity.Order;
import com.library.AmLibrary.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;

    @Getter
    @AllArgsConstructor
    public static class TopSellingItemDTO {
        private String name;
        private Long quantity;
        private BigDecimal totalRevenue;
    }

    @Getter
    @AllArgsConstructor
    public static class HourlySalesDTO {
        private int hour;
        private BigDecimal totalAmount;
        private long ordersCount;
    }

    public List<TopSellingItemDTO> getTopSellingItems(int limit) {
        LocalDateTime thirtyDaysAgo = LocalDate.now().minusDays(30).atStartOfDay();
        List<Order> orders = orderRepository.findByOrderDateAfter(thirtyDaysAgo);

        Map<String, Long> qtyMap = new HashMap<>();
        Map<String, BigDecimal> revMap = new HashMap<>();

        for (Order order : orders) {
            if ("RETURNED".equalsIgnoreCase(order.getStatus())) continue;
            for (OrderItem item : order.getItems()) {
                String name = item.getProduct().getName();
                qtyMap.put(name, qtyMap.getOrDefault(name, 0L) + item.getQuantity());
                BigDecimal subTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                revMap.put(name, revMap.getOrDefault(name, BigDecimal.ZERO).add(subTotal));
            }
        }

        return qtyMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(e -> new TopSellingItemDTO(e.getKey(), e.getValue(), revMap.get(e.getKey())))
                .collect(Collectors.toList());
    }

    public List<HourlySalesDTO> getTodayHourlySales() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<Order> orders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);

        Map<Integer, BigDecimal> salesByHour = new HashMap<>();
        Map<Integer, Long> countByHour = new HashMap<>();

        for (int h = 8; h <= 23; h++) {
            salesByHour.put(h, BigDecimal.ZERO);
            countByHour.put(h, 0L);
        }

        for (Order order : orders) {
            if ("RETURNED".equalsIgnoreCase(order.getStatus())) continue;
            int hour = order.getOrderDate().getHour();
            salesByHour.put(hour, salesByHour.getOrDefault(hour, BigDecimal.ZERO).add(order.getTotalAmount()));
            countByHour.put(hour, countByHour.getOrDefault(hour, 0L) + 1);
        }

        return salesByHour.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new HourlySalesDTO(e.getKey(), e.getValue(), countByHour.get(e.getKey())))
                .collect(Collectors.toList());
    }
}