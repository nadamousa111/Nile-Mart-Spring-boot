package com.route.nilemart.notification;

import com.route.nilemart.model.Customer;

public interface NotificationSender {

    void send(Customer customer, String message);

    default boolean supports(Customer customer) {
        return true;
    }
}