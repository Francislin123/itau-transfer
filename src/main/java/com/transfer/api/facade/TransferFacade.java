package com.transfer.api.facade;

import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.controller.request.TransferRequestDTO;

@FunctionalInterface
public interface TransferFacade {
    TransferResponseDTO makeTransfer(TransferRequestDTO transferRequestDTO);
}
