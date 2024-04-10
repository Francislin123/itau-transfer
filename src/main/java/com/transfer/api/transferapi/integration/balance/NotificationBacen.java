package com.transfer.api.transferapi.integration.balance;

import com.transfer.api.transferapi.integration.balance.request.NotificationBacenRequest;

@FunctionalInterface
public interface NotificationBacen {
    void notificationBacen(final NotificationBacenRequest notificationBacenRequest);
}
