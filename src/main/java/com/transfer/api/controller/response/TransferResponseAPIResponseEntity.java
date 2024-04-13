package com.transfer.api.controller.response;

import org.springframework.http.ResponseEntity;

public class TransferResponseAPIResponseEntity {

    public static ResponseEntity<TransferResponseDTO> getTransferResponseDTOResponseEntity(
            final TransferResponseDTO transferResponseDTO) {

        if (transferResponseDTO.getValor() > 0) {
            return ResponseEntity.ok().body(transferResponseDTO);
        }

        if (transferResponseDTO.getIdTransferencia() == null) {
            return ResponseEntity.notFound().eTag("Client is not found").build();
        }

        return ResponseEntity.ok().body(transferResponseDTO);
    }
}
