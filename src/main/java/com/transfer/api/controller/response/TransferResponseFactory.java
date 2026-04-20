package com.transfer.api.controller.response;

import org.springframework.http.ResponseEntity;

public class TransferResponseFactory {

    public static ResponseEntity<TransferResponseDTO> build(
            final TransferResponseDTO dto) {

        // 🔐 Null safety
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        // 🔴 Regra principal: não existe transferência
        if (dto.getIdTransferencia() == null) {
            return ResponseEntity.notFound().build();
        }

        // 🟡 Regra de negócio: limite inválido
        if (dto.getLimiteDiario() <= 0) {
            return ResponseEntity.unprocessableEntity().build();
        }

        // ✅ Sucesso
        return ResponseEntity.ok(dto);
    }
}