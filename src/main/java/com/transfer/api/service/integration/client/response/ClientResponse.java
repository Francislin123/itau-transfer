package com.transfer.api.service.integration.client.response;

import lombok.Builder;

@Builder
public record ClientResponse(
        String id,
        String name,
        String phone,
        String personType,
        String error // Added to support the fallback message from your ClientImpl
) {
    /**
     * Compact Constructor:
     * Standardizes data or ensures fields are not null if needed.
     */
    public ClientResponse {
        // You can add validation logic here if required
    }
}