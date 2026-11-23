package com.route.nilemart.pricing;

import com.route.nilemart.common.Money;
import com.route.nilemart.model.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class StandardPricingService implements PricingService {

    @Override
    public BigDecimal getPrice(Product product, int quantity) {

        BigDecimal total = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        return Money.scale(total);
    }
}
