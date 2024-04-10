package com.transfer.api.transferapi.impl;

import com.transfer.api.transferapi.controller.TransferResponseDTO;
import com.transfer.api.transferapi.controller.request.TransferRequestDTO;

@FunctionalInterface
public interface Transfer {
    TransferResponseDTO makeTransfer(TransferRequestDTO transferRequestDTO);
}
