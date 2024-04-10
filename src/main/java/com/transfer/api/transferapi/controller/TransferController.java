package com.transfer.api.transferapi.controller;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.impl.TransferFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TransferController.URI_TRANSFER)
public class TransferController {

    public static final String URI_TRANSFER = "/transfer";

    @Autowired
    private TransferFacade transfer;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> makeTransfer(@RequestBody TransferRequestDTO transferRequestDTO) {

        TransferResponseDTO transferResponseDTO = transfer.makeTransfer(transferRequestDTO);

        if (transferResponseDTO.getValor() > 0) {
            return ResponseEntity.ok().body(transferResponseDTO);
        }

        if (transferResponseDTO.getIdTransferencia() == null) {
            return ResponseEntity.notFound().eTag("Client is not found").build();
        }

        return ResponseEntity.ok().body(transferResponseDTO);
    }
}
