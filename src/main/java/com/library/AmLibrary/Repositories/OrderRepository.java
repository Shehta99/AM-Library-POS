package com.library.AmLibrary.Repositories;

import com.library.AmLibrary.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Order> findByOrderDateAfter(LocalDateTime thirtyDaysAgo);
}

