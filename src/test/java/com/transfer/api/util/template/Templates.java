package com.transfer.api.util.template;

import com.transfer.api.controller.request.ContaRequestDto;
import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.client.response.ClientResponse;

public class Templates {

    public static AccountOriginResponse getAccountOriginResponse() {
        return AccountOriginResponse.builder()
                .id("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .saldo(500.0)
                .limiteDiario(600.0)
                .ativo(true)
                .build();
    }

    public static ClientResponse getClientResponse() {
        return ClientResponse.builder()
                .id("d0d32142-74b7-4aca-9c68-838aeacef96b876")
                .nome("Wesley Snipes")
                .telefone("11998764532")
                .tipoPessoa("Fisica")
                .build();
    }

    public static TransferRequestDTO getTransferRequestDTO(ContaRequestDto contaRequestDto) {
        return TransferRequestDTO.builder()
                .idCliente("bcdd1048-a501-4608-bc82-66d7b4db3600")
                .valor(500.00)
                .conta(contaRequestDto)
                .build();
    }

    public static ContaRequestDto getContaRequestDto() {
        return ContaRequestDto.builder()
                .idOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b")
                .idDestino("41313d7b-bd75-4c75-9dea-1f4be434007f")
                .build();
    }
}
