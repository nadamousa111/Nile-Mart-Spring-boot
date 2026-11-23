package com.route.nilemart.pricing;

import com.route.nilemart.common.Money;
import com.route.nilemart.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PromotionalPricingService implements PricingService {

    private final BigDecimal discountAmount;

    public PromotionalPricingService(
            @Value("${store.discount-amount}") BigDecimal discountAmount
    ) {
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal getPrice(Product product, int quantity) {

        BigDecimal total = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        BigDecimal discount = BigDecimal.ONE.subtract(
                discountAmount.divide(new BigDecimal("100"))
        );

        return Money.scale(total.multiply(discount));
    }
}