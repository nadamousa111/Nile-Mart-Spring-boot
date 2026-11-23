package com.route.nilemart.shipping;

import com.route.nilemart.model.Governorate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ShippingQuoteService {

    private final ShippingCalculator normalCalculator;
    private final ShippingCalculator freeCalculator;
    private final BigDecimal freeShippingThreshold;

    public BigDecimal quote(
            Governorate governorate,
            BigDecimal subtotal
    ) {

        if (subtotal.compareTo(freeShippingThreshold) >= 0
                && governorate.getZone().isFreeShippingEligible()) {

            return freeCalculator.calculate(governorate);
        }

        return normalCalculator.calculate(governorate);
    }
}