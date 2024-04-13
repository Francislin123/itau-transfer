package com.transfer.api.util;

import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.controller.request.TransferRequestDTO;

public class CheckTransfer {
    public static boolean checkTransfer(double dailyCustomerLimit, double transferValue) {
        return transferValue == 0.0 || transferValue > dailyCustomerLimit;
    }

    public static boolean isaBoolean(final TransferRequestDTO transferRequestDTO,
                                     final AccountOriginResponse accountOriginResponse) {
        return accountOriginResponse.isAtivo()
                && accountOriginResponse.getSaldo() >= transferRequestDTO.getValor();
    }
}
