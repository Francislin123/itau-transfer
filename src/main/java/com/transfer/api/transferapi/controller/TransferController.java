package com.transfer.api.transferapi.controller;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.facade.TransferFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.transfer.api.transferapi.controller.response.TransferResponseAPIResponseEntity.getTransferResponseDTOResponseEntity;

@RestController
@RequestMapping(TransferController.URI_TRANSFER)
public class TransferController {

    public static final String URI_TRANSFER = "/transfer";

    @Autowired
    private TransferFacade transfer;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDTO> makeTransfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        return getTransferResponseDTOResponseEntity(transfer.makeTransfer(transferRequestDTO));
    }
}
