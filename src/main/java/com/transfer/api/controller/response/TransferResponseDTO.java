package com.transfer.api.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferResponseDTO {

    private String idTransferencia;
    private double limiteDiario;
    private String msg;

    @Builder
    public TransferResponseDTO(String idTransferencia, double limiteDiario, String msg) {
        this.idTransferencia = idTransferencia;
        this.limiteDiario = limiteDiario;
        this.msg = msg;
    }
}
