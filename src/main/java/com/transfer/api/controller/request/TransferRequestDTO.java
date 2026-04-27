package com.transfer.api.controller.request;

import lombok.Builder;

@Builder
public record TransferRequestDTO(
        String idCliente,
        double valor,
        ContaRequestDto conta
) {
    /*
     * O uso de Record remove a necessidade de @Getter, @Setter e construtores manuais.
     * Os campos são acessados via idCliente(), valor() e conta().
     */
}