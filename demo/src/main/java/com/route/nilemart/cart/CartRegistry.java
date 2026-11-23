package com.route.nilemart.cart;

import com.route.nilemart.common.Check;
import com.route.nilemart.common.NotFoundException;
import com.route.nilemart.model.Customer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CartRegistry {

    private final ObjectProvider<ShoppingCart> cartProvider;

    private final AtomicInteger counter = new AtomicInteger(0);

    private final ConcurrentHashMap<Integer, ShoppingCart> carts =
            new ConcurrentHashMap<>();

    public CartRegistry(ObjectProvider<ShoppingCart> cartProvider) {
        this.cartProvider = cartProvider;
    }

    public ShoppingCart open(Customer customer) {

        int number = counter.incrementAndGet();

        ShoppingCart cart = cartProvider.getObject();

        cart.open(number, customer);

        carts.put(number, cart);

        return cart;
    }

    public ShoppingCart get(int number) {

        return Check.found(
                java.util.Optional.ofNullable(carts.get(number)),
                () -> new NotFoundException(
                        "Cart " + number + " was not found."
                )
        );
    }

    public List<ShoppingCart.CartView> views() {

        return carts.values().stream()
                .map(ShoppingCart::view)
                .sorted((a, b) -> Integer.compare(
                        a.number(), b.number()
                ))
                .toList();
    }

    public ShoppingCart getCart(int cartId) {
        return null;
    }

    public ShoppingCart createCart() {
        return null;
    }
}