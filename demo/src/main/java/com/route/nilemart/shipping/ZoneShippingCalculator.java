package com.route.nilemart.shipping;

import com.route.nilemart.model.Governorate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class ZoneShippingCalculator implements ShippingCalculator {

    @Override
    public BigDecimal calculate(Governorate governorate) {
        return governorate.getZone().getFee();
    }
}