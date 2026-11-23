package com.route.nilemart.checkout;


import com.route.nilemart.common.Money;

import java.math.BigDecimal;

public record Quote(
        BigDecimal subtotal,
        BigDecimal vat,
        BigDecimal shippingFee,
        BigDecimal total
) {

    public Quote {
        subtotal = Money.scale(subtotal);
        vat = Money.scale(vat);
        shippingFee = Money.scale(shippingFee);
        total = Money.scale(total);
    }
}