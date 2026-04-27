package com.transfer.api.service.integration.balance.request;

import com.transfer.api.controller.request.ContaRequestDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NotificationBacenRequest(
        UUID id,
        double valor,
        ContaRequestDto conta
) {
    /**
     * Compact Constructor:
     * If the ID is null during instantiation, it automatically generates a new UUID.
     */
    public NotificationBacenRequest {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    /**
     * Optional: Helper constructor for quick instantiation without manual ID.
     */
    public NotificationBacenRequest(double valor, ContaRequestDto conta) {
        this(UUID.randomUUID(), valor, conta);
    }
}