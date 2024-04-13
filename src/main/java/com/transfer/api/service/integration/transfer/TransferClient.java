package com.transfer.api.service.integration.transfer;

import com.transfer.api.controller.request.TransferRequestDTO;

@FunctionalInterface
public interface TransferClient {
    String transferSend(final TransferRequestDTO transferRequestDTO);
}
