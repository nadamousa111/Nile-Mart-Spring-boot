package com.route.nilemart.checkout;

import com.route.nilemart.common.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TaxCalculator {

    private final BigDecimal vatRate;

    public TaxCalculator(
            @Value("${store.vat-rate}") BigDecimal vatRate
    ) {
        this.vatRate = vatRate;
    }

    public BigDecimal calculate(BigDecimal subtotal) {
        return Money.scale(subtotal.multiply(vatRate));
    }
}