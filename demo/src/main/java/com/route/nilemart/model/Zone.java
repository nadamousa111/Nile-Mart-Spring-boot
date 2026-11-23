package com.route.nilemart.model;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public enum Zone {

    GREATER_CAIRO("12.00", true),
    ALEX_DELTA("15.00", true),
    CANAL("18.00", true),
    UPPER_EGYPT("25.00", false),
    REMOTE("40.00", false);

    private final BigDecimal fees;
    private final boolean freeShippingEligible;

    Zone(String fees, boolean freeShippingEligible) {
        this.fees = new BigDecimal(fees);
        this.freeShippingEligible = freeShippingEligible;
    }
}
