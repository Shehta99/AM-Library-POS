package com.library.AmLibrary.Controllers;

import com.library.AmLibrary.Repositories.ProductRepository;
import com.library.AmLibrary.Services.ProductService;
import com.library.AmLibrary.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // تفعيل CORS محلياً لمنع حظر اتصال React
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    // الحصول على جميع المنتجات (كتب وأدوات مدرسية)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // تصفية المنتجات حسب التصنيف (مثلاً BOOK أو STATIONERY)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // الحصول على تفاصيل منتج معين بالـ ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // إضافة منتج جديد للمخزن (كتاب أو أداة مدرسية)
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }
    // داخل ProductController.java
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        // جلب المنتجات التي رصيدها 5 قطع أو أقل
        return ResponseEntity.ok(productRepository.findByStockQuantityLessThanEqual(5));
    }


}
