package com.route.nilemart.notification;

import com.route.nilemart.model.Customer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class EmailNotificationSender extends AbstractNotificationSender {

    @Override
    protected String channel() {
        return "EMAIL";
    }

    @Override
    protected String address(Customer customer) {
        return customer.email();
    }

    @Override
    public boolean supports(Customer customer) {
        return customer.email() != null;
    }
}