package com.transfer.api.transferapi.integration.transfer;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;

@FunctionalInterface
public interface TransferClient {
    String transferClientSend(final TransferRequestDTO transferRequestDTO);
}
