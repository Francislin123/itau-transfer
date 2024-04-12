package com.transfer.api.transferapi.facade;

import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.controller.request.TransferRequestDTO;

@FunctionalInterface
public interface TransferFacade {
    TransferResponseDTO makeTransfer(TransferRequestDTO transferRequestDTO);
}
