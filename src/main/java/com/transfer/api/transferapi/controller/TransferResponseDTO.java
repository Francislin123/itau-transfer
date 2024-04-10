package com.transfer.api.transferapi.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferResponseDTO {

    private String idTransferencia;
    private double valor;

    @Builder
    public TransferResponseDTO(String idTransferencia, double valor) {
        this.idTransferencia = idTransferencia;
        this.valor = valor;
    }
}
