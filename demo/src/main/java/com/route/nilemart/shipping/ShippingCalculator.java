package com.route.nilemart.shipping;

import com.route.nilemart.model.Governorate;
import java.math.BigDecimal;

public interface ShippingCalculator {

    BigDecimal calculate(Governorate governorate);
}