package com.transfer.api.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Factory responsible for determining the correct HTTP status code
 * based on the transfer orchestration results.
 */
public class TransferResponseFactory {

    public static ResponseEntity<TransferResponseDTO> build(final TransferResponseDTO dto) {

        // 🔐 Null safety check
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        // 1º Check Business Rules (e.g., Daily Limit)
        // If it is a limit-related error, we return 422 even without a transfer ID
        if ("Daily limit exceeded".equals(dto.msg()) || "Invalid transfer amount".equals(dto.msg())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(dto);
        }

        // 2º If there is no ID and it's not a limit error, then it is a resource error (404)
        if (dto.idTransferencia() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }

        // ✅ Success path
        return ResponseEntity.ok(dto);
    }
}