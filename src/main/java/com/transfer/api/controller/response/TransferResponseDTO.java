package com.transfer.api.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferResponseDTO {

    private String idTransferencia;
    private double limiteDiario;

    @Builder
    public TransferResponseDTO(String idTransferencia, double limiteDiario) {
        this.idTransferencia = idTransferencia;
        this.limiteDiario = limiteDiario;
    }
}
