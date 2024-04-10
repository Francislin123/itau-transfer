package com.transfer.api.transferapi.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDTO {
    private String idCliente;
    private double valor;
    private ContaRequestDto conta;
}
