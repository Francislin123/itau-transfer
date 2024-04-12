package com.transfer.api.transferapi.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDTO {
    private String idCliente;
    private double valor;
    private ContaRequestDto conta;

    @Builder
    public TransferRequestDTO(String idCliente, double valor, ContaRequestDto conta) {
        this.idCliente = idCliente;
        this.valor = valor;
        this.conta = conta;
    }
}
