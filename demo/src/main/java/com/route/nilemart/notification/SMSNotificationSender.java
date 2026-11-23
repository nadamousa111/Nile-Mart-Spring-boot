package com.route.nilemart.notification;

import com.route.nilemart.model.Customer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SMSNotificationSender extends AbstractNotificationSender {

    @Override
    protected String channel() {
        return "SMS";
    }

    @Override
    protected String address(Customer customer) {
        return customer.phone();
    }
}