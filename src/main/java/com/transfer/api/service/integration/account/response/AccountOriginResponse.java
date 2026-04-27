package com.transfer.api.service.integration.account.response;

import lombok.Builder;

@Builder
public record AccountOriginResponse(
        String id,
        String erro,
        double saldo,
        double limiteDiario,
        boolean ativo
) {
    /* * The canonical constructor and @Builder are handled by Lombok.
     * Records automatically provide:
     * - Private final fields
     * - Getter-like methods: id(), erro(), saldo(), etc.
     * - toString(), equals(), and hashCode()
     */
}