package com.transfer.api.transferapi.integration.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountOriginResponse {
    private String id;
    private double saldo;
    private double limiteDiario;
    private boolean ativo;

    @Builder
    public AccountOriginResponse(String id, double saldo, double limiteDiario, boolean ativo) {
        this.id = id;
        this.saldo = saldo;
        this.limiteDiario = limiteDiario;
        this.ativo = ativo;
    }
}
