package com.transfer.api.controller.request;

import lombok.Builder;

@Builder
public record ContaRequestDto(
        String idOrigem,
        String idDestino
) {
    /*
     * Records automatically provide:
     * - Private final fields
     * - Accessor methods: originId(), destinationId()
     * - Canonical constructor
     * - toString(), equals(), and hashCode()
     */
}