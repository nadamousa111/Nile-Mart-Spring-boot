package com.route.nilemart.checkout;
import com.route.nilemart.cart.ShoppingCart;
import com.route.nilemart.common.OutOfStockException;
import com.route.nilemart.common.StoreException;
import com.route.nilemart.model.Customer;
import com.route.nilemart.model.Governorate;
import com.route.nilemart.notification.NotificationSender;
import com.route.nilemart.model.Order;
import com.route.nilemart.model.OrderLine;
import com.route.nilemart.service.ProductService;
import com.route.nilemart.shipping.ShippingQuoteService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    public CheckoutService(
            ProductService productService,
            ShippingQuoteService shippingQuoteService,
            TaxCalculator taxCalculator,
            OrderRegistry orderRegistry,
            List<NotificationSender> notificationSenders
    ) {
        this.productService = productService;
        this.shippingQuoteService = shippingQuoteService;
        this.taxCalculator = taxCalculator;
        this.orderRegistry = orderRegistry;
        this.notificationSenders = notificationSenders;
    }

    public Quote quote(
            ShoppingCart cart,
            Governorate governorate
    ) {
        if (cart.isEmpty()) {
            throw new StoreException("Cart is empty.");
        }

        BigDecimal subtotal = cart.subtotal();
        BigDecimal vat = taxCalculator.calculate(subtotal);
        BigDecimal shipping = shippingQuoteService.quote(
                governorate, subtotal
        );

        BigDecimal total = subtotal.add(vat).add(shipping);

        return new Quote(subtotal, vat, shipping, total);
    }

    public synchronized Order placeOrder(
            ShoppingCart cart,
            Customer customer, Governorate governorate
    ) {
        if (cart.isEmpty()) {
            throw new StoreException("Cart is empty.");
        }

        // Check stock before changing any quantities.
        cart.lines().forEach((SKU, quantity) -> {
            if (productService.findBySKU(SKU).getStockQuantity() < quantity) {
                throw new OutOfStockException(
                        "Not enough stock for " + SKU + "."
                );
            }
        });

        Quote quote = quote(cart, governorate);

        List<OrderLine> orderLines = cart.view().lines().stream()
                .map(line -> new OrderLine(
                        line.SKU(),
                        line.name()
                ))
                .toList();

        cart.lines().forEach(productService::reduceStock);

        String orderNumber = orderRegistry.nextOrderNumber();

        Order order = new Order(
                orderNumber,
                cart.getCustomer(),
                governorate,
                orderLines,
                quote.subtotal(),
                quote.vat(),
                quote.shippingFee(),
                quote.total()
        );

        orderRegistry.save(order);

        String message = "Your order " + orderNumber + " has been confirmed.";

        notificationSenders.forEach(
                sender -> sender.send(cart.getCustomer(), message)
        );

        cart.clear();

        return order;
    }
}