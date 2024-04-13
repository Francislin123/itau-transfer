package com.transfer.api.integration.balance;

import com.transfer.api.integration.balance.request.NotificationBacenRequest;

@FunctionalInterface
public interface NotificationBacen {
    void notificationBacen(final NotificationBacenRequest notificationBacenRequest);
}
