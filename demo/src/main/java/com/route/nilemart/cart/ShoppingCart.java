package com.route.nilemart.cart;

import com.route.nilemart.common.Money;
import com.route.nilemart.common.OutOfStockException;
import com.route.nilemart.common.StoreException;
import com.route.nilemart.model.Customer;
import com.route.nilemart.model.Product;
import com.route.nilemart.service.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Getter
public class ShoppingCart {

    private final ProductService productService;
    private final int maxQuantity;

    private final Map<String, Integer> items = new LinkedHashMap<>();

    private int number;
    private Customer customer;
    private boolean opened;
    private int id;

    public ShoppingCart(
            ProductService productService,
            @Value("${store.max-quantity-per-product}") int maxQuantity
    ) {
        this.productService = productService;
        this.maxQuantity = maxQuantity;
    }

    public void open(int number, Customer customer) {

        if (opened) {
            throw new IllegalStateException("Cart is already opened.");
        }

        this.number = number;
        this.customer = customer;
        this.opened = true;
    }

    public synchronized void addItem(String SKU, int quantity) {

        if (quantity < 1) {
            throw new StoreException("Quantity must be at least 1.");
        }

        Product product = productService.findBySKU(SKU);

        if (product.getStockQuantity() == 0) {
            throw new OutOfStockException(
                    product.getProductName() + " is out of stock."
            );
        }

        int newQuantity = items.getOrDefault(SKU, 0) + quantity;

        if (newQuantity > maxQuantity) {
            throw new StoreException(
                    "You can add at most " + maxQuantity
                            + " of " + product.getProductName() + "."
            );
        }

        if (newQuantity > product.getStockQuantity()) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity()
                            + " available for " + product.getProductName() + "."
            );
        }

        items.put(SKU, newQuantity);
    }

    public BigDecimal subtotal() {

        return items.entrySet().stream()
                .map(entry -> productService.getPrice(
                        entry.getKey(), entry.getValue()
                ))
                .reduce(Money.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int itemCount() {
        return items.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Map<String, Integer> lines() {
        return Collections.unmodifiableMap(items);
    }

    public void clear() {
        items.clear();
    }

    public CartView view() {

        List<LineView> lines = items.entrySet().stream()
                .map(entry -> {
                    Product product = productService.findBySKU(entry.getKey());

                    return new LineView(
                            product.getSKU(),
                            product.getProductName(),
                            entry.getValue(),
                            productService.getPrice(
                                    entry.getKey(), entry.getValue()
                            )
                    );
                })
                .toList();

        return new CartView(
                number,
                customer,
                lines,
                itemCount(),
                subtotal(),
                isEmpty()
        );
    }


    public record LineView(
            String SKU,
            String name,
            int quantity,
            BigDecimal linePrice
    ) {
    }

    public record CartView(
            int number,
            Customer customer,
            List<LineView> lines,
            int itemCount,
            BigDecimal subtotal,
            boolean empty
    ) {
        public CartView {
            lines = List.copyOf(lines);
        }
    }
}
