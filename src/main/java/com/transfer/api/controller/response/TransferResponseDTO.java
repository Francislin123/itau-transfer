package com.transfer.api.controller.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TransferResponseDTO(
        String idTransferencia,
        double limiteDiario,
        String msg
) {
    /*
     * Records removem a necessidade de @Getter e @Setter.
     * O acesso agora é feito via métodos:
     * - response.idTransferencia()
     * - response.limiteDiario()
     * - response.msg()
     */
}