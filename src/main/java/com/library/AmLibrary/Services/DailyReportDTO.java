package com.library.AmLibrary.Services;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class DailyReportDTO {
    private LocalDate reportDate;
    private Long totalOrders;        // عدد الفواتير
    private BigDecimal totalRevenue; // إجمالي المبيعات (الدخل)
    private BigDecimal totalCost;    // إجمالي التكلفة
    private BigDecimal netProfit;    // صافي الأرباح
    private Double profitMargin;     // نسبة هامش الربح %
}
