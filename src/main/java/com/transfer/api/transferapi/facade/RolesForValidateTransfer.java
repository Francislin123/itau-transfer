package com.transfer.api.transferapi.facade;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;

@FunctionalInterface
public interface RolesForValidateTransfer {
    TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO);
}
