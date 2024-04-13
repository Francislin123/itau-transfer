package com.transfer.api.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaRequestDto {
    private String idOrigem;
    private String idDestino;

    @Builder
    public ContaRequestDto(String idOrigem, String idDestino) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
    }
}
