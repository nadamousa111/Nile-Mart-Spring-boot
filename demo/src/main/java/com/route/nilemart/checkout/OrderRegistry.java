package com.route.nilemart.checkout;

import com.route.nilemart.model.Order;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderRegistry {

    private final Clock clock;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public OrderRegistry(Clock clock) {
        this.clock = clock;
    }

    public String nextOrderNumber() {

        String date = LocalDate.now(clock)
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format(
                "NM-%s-%05d",
                date,
                counter.incrementAndGet()
        );
    }

    public void save(Order order) {
        orders.add(order);
    }

    public Optional<Order> findByNumber(String orderNumber) {

        return orders.stream()
                .filter(order -> order.orderNumber().equals(orderNumber))
                .findFirst();
    }

    public List<Order> findAll() {

        return orders.stream()
                .sorted((a, b) ->
                        b.orderNumber().compareTo(a.orderNumber()))
                .toList();
    }

    public BigDecimal revenue() {

        return orders.stream()
                .map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PreDestroy
    public void close() {
        System.out.println("End of day revenue: " + revenue());
    }
}